package com.gts.supplychain.service.movement.mapper;

import com.gts.supplychain.api.movement.dto.MovementCreateRequest;
import com.gts.supplychain.model.entity.Movement;
import com.gts.supplychain.model.entity.Product;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface MovementServiceMapper {

	@BeanMapping(ignoreByDefault = true)
	@Mapping(target = "fromLocation",source = "movementRequest.fromLocation")
	@Mapping(target = "toLocation",source = "movementRequest.toLocation")
	@Mapping(target = "movementDate",source = "movementRequest.movementDate")
	@Mapping(target = "product",source = "product")
	Movement toMovement( Product product, MovementCreateRequest movementCreateRequest);




}
