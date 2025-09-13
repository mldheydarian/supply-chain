package com.gts.supplychain.service.product;

import com.gts.supplychain.spec.product.request.ProductCreateRequest;
import com.gts.supplychain.exception.BusinessException;
import com.gts.supplychain.model.entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

	Product getByProductId(String productId) throws BusinessException;

	Page<Product> getAllProducts(Pageable pageable);

	Product createProduct(ProductCreateRequest model);

}
