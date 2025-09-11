package com.gts.supplychain.service.product.impl;

import org.springframework.stereotype.Service;

import java.util.List;

import com.gts.supplychain.api.product.dto.ProductCreateRequest;
import com.gts.supplychain.model.repository.ProductRepository;
import com.gts.supplychain.model.entity.Product;
import com.gts.supplychain.service.product.ProductService;
import com.gts.supplychain.service.product.mapper.ProductServiceMapper;
import lombok.extern.slf4j.Slf4j;

	@Service
	@Slf4j
	public class ProductServiceImpl implements ProductService {

		private final ProductRepository productRepository;

		private final ProductServiceMapper mapper;

		public ProductServiceImpl(ProductRepository productRepository, ProductServiceMapper mapper) {
			this.productRepository = productRepository;
			this.mapper = mapper;
		}


	@Override
	public Product getByProductId(String productId) {
		return productRepository.findByProductId(productId)
				.orElseThrow(() -> new RuntimeException("Product not found"));
	}

	@Override
    public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	@Override
	public Product createProduct(ProductCreateRequest model) {
		return productRepository.save(mapper.toProduct(model));

	}
}
