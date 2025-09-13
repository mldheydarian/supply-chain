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
import com.gts.supplychain.spec.movement.request.MovementCreateRequest;
import com.gts.supplychain.spec.movement.response.MovementResponse;
import com.gts.supplychain.spec.product.request.ProductCreateRequest;
import com.gts.supplychain.spec.common.response.PageableResponse;

import com.gts.supplychain.model.entity.Movement;
import com.gts.supplychain.model.entity.Product;
import com.gts.supplychain.model.repository.MovementRepository;
import com.gts.supplychain.model.repository.ProductRepository;
import com.gts.supplychain.service.movement.MovementService;
import com.gts.supplychain.service.movement.mapper.MovementServiceMapper;
import com.gts.supplychain.service.product.ProductService;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	private MovementRepository movementRepository;

	@Autowired
	private TestRestTemplate restTemplate;

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


	@Test
	@DisplayName("movement ordering - should return movements sorted by movementDate ascending")
	void geMovementOrdering_1() {
		Product product = new Product();
		product.setProductId("P-350");
		product.setType("Type-W");
		product.setOrigin("Factory-K");
		product.setManufacturingDate(LocalDateTime.now());
		productRepository.save(product);

		Movement m1 = new Movement();
		m1.setProduct(product);
		m1.setFromLocation("Start");
		m1.setToLocation("Mid");
		m1.setMovementDate(LocalDateTime.now());

		Movement m2 = new Movement();
		m2.setProduct(product);
		m2.setFromLocation("Mid");
		m2.setToLocation("End");
		m2.setMovementDate(LocalDateTime.now().plusHours(1));

		movementRepository.saveAll(List.of(m2, m1)); // intentionally reversed

		ResponseEntity<PageableResponse<MovementResponse>> response = restTemplate.exchange(
				"/api/products/P-350/movements?page=0&size=10",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<PageableResponse<MovementResponse>>() {}
		);

		// Assert: check chronological order
		assertEquals(HttpStatus.OK, response.getStatusCode());
		PageableResponse<MovementResponse> page = response.getBody();
		assertNotNull(page);
		assertEquals(2, page.getContent().size());

		List<LocalDateTime> movementDates = page.getContent().stream()
				.map(MovementResponse::getMovementDate)
				.toList();

		assertTrue(movementDates.get(0).isBefore(movementDates.get(1)) || movementDates.get(0).isEqual(movementDates.get(1)),
				"Movements should be sorted by movementDate in ascending order");
	}

	@Test
	@DisplayName("movement immutability - should throw exception when trying to update a persisted movement")
	void movementImmutability_shouldPreventUpdate() {
		Product product = new Product();
		product.setProductId("P-400");
		product.setType("Type-Y");
		product.setOrigin("Factory-N");
		product.setManufacturingDate(LocalDateTime.now());
		productRepository.save(product);

		Movement movement = new Movement();
		movement.setProduct(product);
		movement.setFromLocation("Start");
		movement.setToLocation("End");
		movement.setMovementDate(LocalDateTime.now());

		movementRepository.saveAndFlush(movement);
		assertEquals(movementRepository.findAll().size(),1);

		Movement persisted = movementRepository.findById(movement.getId()).orElseThrow();
		persisted.setToLocation("Modified-End");
		movementRepository.save(persisted);
		Movement reloaded = movementRepository.findById(movement.getId()).orElseThrow();
		assertEquals("End", reloaded.getToLocation());

	}


}