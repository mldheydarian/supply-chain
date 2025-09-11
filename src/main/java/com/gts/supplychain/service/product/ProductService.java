package com.gts.supplychain.service.product;

import java.util.List;

import com.gts.supplychain.api.product.dto.ProductCreateRequest;
import com.gts.supplychain.model.entity.Product;

public interface ProductService {

	Product getByProductId(String productId);

	List<Product> getAllProducts();

	Product createProduct(ProductCreateRequest model);

}
