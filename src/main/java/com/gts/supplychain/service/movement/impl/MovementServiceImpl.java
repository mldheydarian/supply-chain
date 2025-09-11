package com.gts.supplychain.service.movement.impl;

import org.springframework.stereotype.Service;

import java.util.List;

import com.gts.supplychain.api.movement.dto.MovementCreateRequest;
import com.gts.supplychain.exception.BusinessException;
import com.gts.supplychain.model.repository.MovementRepository;
import com.gts.supplychain.model.entity.Movement;
import com.gts.supplychain.model.entity.Product;
import com.gts.supplychain.service.movement.MovementService;
import com.gts.supplychain.service.movement.mapper.MovementServiceMapper;
import com.gts.supplychain.service.product.impl.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovementServiceImpl implements MovementService {

	private final MovementRepository movementRepository;

	private final ProductServiceImpl productServiceImpl;

	private final MovementServiceMapper mapper;


	@Override
	public Movement recordMovement(String productId, MovementCreateRequest movementCreateRequest) throws BusinessException {
		log.debug("record movement called for productId: {}, request: {}", productId, movementCreateRequest);
		Product product = productServiceImpl.getByProductId(productId);
		Movement movement = movementRepository.save(mapper.toMovement(product, movementCreateRequest));
		log.debug("recording movement saved successfully, movementId: {}", movement.getId());
		return movement;
	}

	@Override
	public List<Movement> getMovements(String productId) throws BusinessException {
		log.debug("get movements called for productId: {}", productId);
		Product product = productServiceImpl.getByProductId(productId);
		List<Movement> movements = movementRepository.findByProductOrderByMovementDateAsc(product);
		log.debug("get movements returned {} records", movements.size());
		return movements;
	}
}
