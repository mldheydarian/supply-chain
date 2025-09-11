package com.gts.supplychain.api.resource.product.mapper;

import java.util.List;

import com.gts.supplychain.api.dto.ProductResponse;
import com.gts.supplychain.model.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductResourceMapper {


	ProductResponse toProductResponse(Product product);

	default List<ProductResponse> toListOfProductResponse(List<Product> productList) {
		return productList.stream().map(this::toProductResponse).toList();

	}


}
