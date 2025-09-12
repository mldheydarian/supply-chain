package com.gts.supplychain.service.movement.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import com.gts.supplychain.AbstractIntegrationTest;
import com.gts.supplychain.api.movement.dto.MovementCreateRequest;
import com.gts.supplychain.api.product.dto.ProductCreateRequest;
import com.gts.supplychain.model.entity.Product;
import com.gts.supplychain.model.repository.ProductRepository;
import com.gts.supplychain.service.movement.MovementService;
import com.gts.supplychain.service.movement.mapper.MovementServiceMapper;
import com.gts.supplychain.service.product.ProductService;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import static org.junit.jupiter.api.Assertions.*;

class MovementServiceImplIntegrationTest extends AbstractIntegrationTest {

	@Autowired
	private MovementService movementService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private MovementServiceMapper mapper;

	@AfterEach
	void cleanUp() {
		productRepository.deleteAll();
	}

	@Test
	@DisplayName("Optimistic lock - should fail when two threads update the same Product concurrently")
	void shouldFailOnConcurrentProductUpdate() {
		ProductCreateRequest request = new ProductCreateRequest();
		request.setProductId("p-optimistic");
		request.setType("type-1");
		request.setOrigin("factory-A");
		request.setManufacturingDate(LocalDateTime.now());

		Product product = productService.createProduct(request);
		Product p1 = productRepository.findById(product.getId()).orElseThrow();
		Product p2 = productRepository.findById(product.getId()).orElseThrow();

		p1.setOrigin("factory-X");
		productRepository.saveAndFlush(p1);

		p2.setOrigin("factory-Y");
		assertThrows(ObjectOptimisticLockingFailureException.class,
				() -> productRepository.saveAndFlush(p2));
	}

	@Test
	@DisplayName("Optimistic lock with Movements - at least one thread should fail on concurrent insert")
	void shouldFailOnConcurrentRecordMovement() throws InterruptedException {
		ProductCreateRequest request = new ProductCreateRequest();
		request.setProductId("p-optimistic");
		request.setType("type-1");
		request.setOrigin("factory-A");
		request.setManufacturingDate(LocalDateTime.now());
		Product product = productService.createProduct(request);

		ExecutorService executor = Executors.newFixedThreadPool(2);
		CountDownLatch latch = new CountDownLatch(2);
		List<String> results = Collections.synchronizedList(new ArrayList<>());

		Runnable task = () -> {
			try {
				MovementCreateRequest movementRequest = new MovementCreateRequest();
				movementRequest.setFromLocation("A");
				movementRequest.setToLocation("B");
				/*
				  Add a tiny random offset to the movementDate to avoid DB unique constraint violations
				  when multiple threads insert a Movement for the same product simultaneously.
				 */
				movementRequest.setMovementDate(LocalDateTime.now().plusNanos(ThreadLocalRandom.current().nextInt(1_000_000)));
				movementService.recordMovement(product.getProductId(), movementRequest);

				results.add("SUCCESS by " + Thread.currentThread().getName());
			} catch (OptimisticLockException | ObjectOptimisticLockingFailureException e) {
				results.add("LOCK_FAIL by " + Thread.currentThread().getName() + " -> " + e.getClass().getSimpleName());
			} catch (Exception e) {
				results.add("OTHER_FAIL by " + Thread.currentThread().getName() + " -> " + e.getClass().getSimpleName());
			} finally {
				latch.countDown();
			}
		};

		executor.submit(task);
		executor.submit(task);

		latch.await();
		executor.shutdown();
		results.forEach(System.out::println);
		boolean hasLockFail = results.stream().anyMatch(r -> r.startsWith("LOCK_FAIL"));
		assertTrue(hasLockFail, "At least one thread should fail due to optimistic lock");
	}

}