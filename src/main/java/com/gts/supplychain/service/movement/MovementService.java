package com.gts.supplychain.service.movement;

import com.gts.supplychain.api.movement.dto.MovementCreateRequest;
import com.gts.supplychain.exception.BusinessException;
import com.gts.supplychain.model.entity.Movement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovementService {

	Movement recordMovement(String productId, MovementCreateRequest movementCreateRequest) throws BusinessException;

	Page<Movement> getMovements(String productId, Pageable pageable) throws BusinessException;
}
