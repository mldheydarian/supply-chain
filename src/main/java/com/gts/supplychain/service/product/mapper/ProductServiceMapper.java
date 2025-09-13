package com.gts.supplychain.service.product.mapper;

import com.gts.supplychain.spec.product.request.ProductCreateRequest;
import com.gts.supplychain.model.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductServiceMapper {

	Product toProduct(ProductCreateRequest request);


}
