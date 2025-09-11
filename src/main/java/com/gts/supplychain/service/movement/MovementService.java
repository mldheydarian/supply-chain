package com.gts.supplychain.service.movement;

import java.util.List;

import com.gts.supplychain.api.movement.dto.MovementCreateRequest;
import com.gts.supplychain.model.entity.Movement;

public interface MovementService  {

	Movement recordMovement(String productId, MovementCreateRequest movementCreateRequest);

	List<Movement> getMovements(String productId);
}
