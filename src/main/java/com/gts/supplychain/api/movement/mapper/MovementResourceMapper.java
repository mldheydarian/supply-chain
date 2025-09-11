package com.gts.supplychain.api.movement.mapper;


import java.util.List;

import com.gts.supplychain.api.movement.dto.MovementResponse;
import com.gts.supplychain.model.entity.Movement;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MovementResourceMapper {
	MovementResponse toMovementResponse(Movement movement);

	default List<MovementResponse> toListOfMovementResponse(List<Movement> movements){
		return movements.stream().map(this::toMovementResponse).toList();

	}
}
