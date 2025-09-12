package com.gts.supplychain.api.product;


import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gts.supplychain.api.product.dto.ProductCreateRequest;
import com.gts.supplychain.api.product.dto.ProductResponse;
import com.gts.supplychain.dto.common.response.PageableResponse;
import com.gts.supplychain.dto.common.request.PagingRequest;

import com.gts.supplychain.api.product.mapper.ProductResourceMapper;
import com.gts.supplychain.exception.BusinessException;
import com.gts.supplychain.model.entity.Product;
import com.gts.supplychain.service.product.impl.ProductServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductResource {

	private final ProductServiceImpl productServiceImpl;

	private final ProductResourceMapper mapper;


	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductCreateRequest request) {
		log.info("START create product with request: {}", request);
		Product product = productServiceImpl.createProduct(request);
		log.info("END create product successfully, productId: {}", product.getId());
		return ResponseEntity.ok(mapper.toProductResponse(product));
	}

	@GetMapping(path = "/{productId}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductResponse> getProduct(@PathVariable String productId) throws BusinessException {
		log.info("START get product with productId: {}", productId);
		Product product = productServiceImpl.getByProductId(productId);
		ProductResponse response = mapper.toProductResponse(product);
		log.info("END get product successfully, productId: {}", product.getId());
		return ResponseEntity.ok(response);
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PageableResponse<ProductResponse>> getAllProducts(@Valid PagingRequest pagingRequest) {
		log.info("START get all products with paging: {}",pagingRequest);
		Page<Product> productsPage = productServiceImpl.getAllProducts(pagingRequest.toPageable());
		PageableResponse<ProductResponse> response = new PageableResponse<>(
			productsPage.map(mapper::toProductResponse));
		log.info("End get all products with paging successfully");
		return ResponseEntity.ok(response);

	}

}
