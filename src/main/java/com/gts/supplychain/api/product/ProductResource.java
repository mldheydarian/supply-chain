package com.gts.supplychain.api.product;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.gts.supplychain.api.product.dto.ProductResponse;
import com.gts.supplychain.api.product.dto.ProductCreateRequest;
import com.gts.supplychain.api.product.mapper.ProductResourceMapper;
import com.gts.supplychain.model.entity.Product;
import com.gts.supplychain.service.product.impl.ProductServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductResource {

    private final ProductServiceImpl productServiceImpl;

	private final ProductResourceMapper mapper;


	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> createProduct(	@Valid  @RequestBody ProductCreateRequest request) {
		Product product = productServiceImpl
				.createProduct(request);
		return ResponseEntity.ok(mapper.toProductResponse(product));

    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable String productId){
        return ResponseEntity.ok(mapper
				.toProductResponse(productServiceImpl.getByProductId(productId)));
    }

    @GetMapping()
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
       return ResponseEntity.ok(mapper.toListOfProductResponse(productServiceImpl.getAllProducts()));
    }
}
