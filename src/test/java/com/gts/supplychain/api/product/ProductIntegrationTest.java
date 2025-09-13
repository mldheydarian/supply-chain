package com.gts.supplychain.api.product;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gts.supplychain.AbstractIntegrationTest;
import com.gts.supplychain.spec.product.request.ProductCreateRequest;
import com.gts.supplychain.spec.product.response.ProductResponse;
import com.gts.supplychain.spec.common.response.PageableResponse;
import com.gts.supplychain.model.entity.Product;
import com.gts.supplychain.model.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class ProductIntegrationTest extends AbstractIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setup() {
		productRepository.deleteAll();
	}


	@Test
	@DisplayName("create product - should save product successfully")
	void createProduct_1() {
		ProductCreateRequest request = new ProductCreateRequest();
		request.setProductId("P-001");
		request.setType("Type-A");
		request.setOrigin("Factory-X");
		request.setManufacturingDate(LocalDateTime.now());

		ResponseEntity<ProductResponse> response = restTemplate.postForEntity(
				"/products", request, ProductResponse.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("P-001", response.getBody().getProductId());

		Product saved = productRepository.findByProductId("P-001").orElseThrow();
		assertEquals("Factory-X", saved.getOrigin());
	}


	@Test
	@DisplayName("create product via controller - should return error when productId already exists")
	void createProduct_2() {
		ProductCreateRequest request = new ProductCreateRequest();
		request.setProductId("p-duplicate");
		request.setType("type-1");
		request.setOrigin("factory-A");
		request.setManufacturingDate(LocalDateTime.now());

		ResponseEntity<ProductResponse> response = restTemplate.postForEntity(
				"/products", request, ProductResponse.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());

		ProductCreateRequest duplicateRequest = new ProductCreateRequest();
		duplicateRequest.setProductId("p-duplicate");
		duplicateRequest.setType("type-2");
		duplicateRequest.setOrigin("factory-B");
		duplicateRequest.setManufacturingDate(LocalDateTime.now());

		ResponseEntity<String> response2 = restTemplate.postForEntity(
				"/products", duplicateRequest, String.class);

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response2.getStatusCode());
	}
	@Test
	@DisplayName("get product by ID - should return existing product")
	void getProductById_1() {
		Product product = new Product();
		product.setProductId("P-002");
		product.setType("Type-B");
		product.setOrigin("Factory-Y");
		product.setManufacturingDate(LocalDateTime.now());
		productRepository.save(product);

		ResponseEntity<ProductResponse> response = restTemplate.getForEntity(
				"/products/{productId}", ProductResponse.class, "P-002"
		);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("P-002", response.getBody().getProductId());
		assertEquals("Type-B", response.getBody().getType());
	}

	@Test
	@DisplayName("get all products - should return paged response")
	void getAllProducts_shouldReturnPagedResponse() {
		Product p1 = new Product();
		p1.setProductId("P-101");
		p1.setType("Type-A");
		p1.setOrigin("Factory-X");
		p1.setManufacturingDate(LocalDateTime.now());

		Product p2 = new Product();
		p2.setProductId("P-102");
		p2.setType("Type-B");
		p2.setOrigin("Factory-Y");
		p2.setManufacturingDate(LocalDateTime.now());

		productRepository.saveAll(List.of(p1, p2));

		ResponseEntity<PageableResponse<ProductResponse>> response = restTemplate.exchange(
				"/products?page=0&size=10",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<PageableResponse<ProductResponse>>() {}
		);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		PageableResponse<ProductResponse> page = response.getBody();
		assertNotNull(page);
		assertEquals(2, page.getContent().size());
		assertEquals(0, page.getPageNumber());
		assertEquals(10, page.getPageSize());
		assertEquals(2, page.getTotalElements());
		assertTrue(page.isLast());

		List<String> ids = page.getContent().stream()
				.map(ProductResponse::getProductId)
				.toList();

		assertTrue(ids.contains("P-101"));
		assertTrue(ids.contains("P-102"));
	}


}