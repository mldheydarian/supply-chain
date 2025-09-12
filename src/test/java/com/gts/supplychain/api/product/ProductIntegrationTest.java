package com.gts.supplychain.api.product;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gts.supplychain.AbstractIntegrationTest;
import com.gts.supplychain.api.product.dto.ProductCreateRequest;
import com.gts.supplychain.api.product.dto.ProductResponse;
import com.gts.supplychain.model.entity.Product;
import com.gts.supplychain.model.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
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
	@DisplayName("get all products - should return list of products")
	void getAllProducts_1() {
		Product p1 = new Product();
		p1.setProductId("P-003");
		p1.setType("Type-C");
		p1.setOrigin("Factory-Z");
		p1.setManufacturingDate(LocalDateTime.now());

		Product p2 = new Product();
		p2.setProductId("P-004");
		p2.setType("Type-D");
		p2.setOrigin("Factory-W");
		p2.setManufacturingDate(LocalDateTime.now());

		productRepository.saveAll(List.of(p1, p2));

		ResponseEntity<ProductResponse[]> response = restTemplate.getForEntity(
				"/products", ProductResponse[].class
		);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		ProductResponse[] products = response.getBody();
		assertNotNull(products);
		assertEquals(2, products.length);
		assertEquals("P-003", products[0].getProductId());
		assertEquals("P-004", products[1].getProductId());
	}





}