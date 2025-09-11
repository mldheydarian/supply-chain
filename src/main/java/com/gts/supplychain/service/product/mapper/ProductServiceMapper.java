package com.gts.supplychain.service.product.mapper;

import com.gts.supplychain.api.product.dto.ProductCreateRequest;
import com.gts.supplychain.model.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductServiceMapper {

	Product toProduct(ProductCreateRequest request);


}
