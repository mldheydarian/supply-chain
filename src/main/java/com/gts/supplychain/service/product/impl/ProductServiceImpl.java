package com.gts.supplychain.service.product.impl;

import org.springframework.stereotype.Service;

import java.util.List;

import com.gts.supplychain.api.product.dto.ProductCreateRequest;
import com.gts.supplychain.exception.BusinessException;
import com.gts.supplychain.exception.NotFoundException;
import com.gts.supplychain.model.repository.ProductRepository;
import com.gts.supplychain.model.entity.Product;
import com.gts.supplychain.service.product.ProductService;
import com.gts.supplychain.service.product.mapper.ProductServiceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	private final ProductServiceMapper mapper;


	@Override
	public Product createProduct(ProductCreateRequest model) {
		log.debug("createProduct called with request: {}", model);
		Product product = productRepository.save(mapper.toProduct(model));
		log.debug("createProduct saved successfully, productId: {}", product.getId());
		return product;
	}

	@Override
	public Product getByProductId(String productId) throws BusinessException {
		log.debug("getByProductId called with productId: {}", productId);
		Product product = productRepository.findByProductId(productId)
				.orElseThrow(() -> new NotFoundException("Product not found"));
		log.debug("getByProductId returning productId: {}", product.getId());
		return product;
	}

	@Override
	public List<Product> getAllProducts() {
		log.debug("getAllProducts called");
		List<Product> products = productRepository.findAll();
		log.debug("getAllProducts returning {} products", products.size());
		return products;
	}

}
