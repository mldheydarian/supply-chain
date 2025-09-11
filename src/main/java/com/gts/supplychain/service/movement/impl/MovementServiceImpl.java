package com.gts.supplychain.service.movement.impl;

import org.springframework.stereotype.Service;

import java.util.List;

import com.gts.supplychain.api.movement.dto.MovementCreateRequest;
import com.gts.supplychain.model.repository.MovementRepository;
import com.gts.supplychain.model.entity.Movement;
import com.gts.supplychain.model.entity.Product;
import com.gts.supplychain.service.movement.MovementService;
import com.gts.supplychain.service.movement.mapper.MovementServiceMapper;
import com.gts.supplychain.service.product.impl.ProductServiceImpl;

@Service
public class MovementServiceImpl implements MovementService {

    private final MovementRepository movementRepository;
	private final ProductServiceImpl productServiceImpl;
    private final MovementServiceMapper mapper;

	public MovementServiceImpl(MovementRepository movementRepository, ProductServiceImpl productServiceImpl, MovementServiceMapper mapper) {
		this.movementRepository = movementRepository;
		this.productServiceImpl = productServiceImpl;
		this.mapper = mapper;
	}


	@Override
	public Movement recordMovement(String productId, MovementCreateRequest movementCreateRequest) {
		Product product = productServiceImpl.getByProductId(productId);
		return  movementRepository.save(mapper.toMovement(product, movementCreateRequest));
	}

	@Override
    public List<Movement> getMovements(String productId) {
		Product product = productServiceImpl.getByProductId(productId);
		return movementRepository.findByProductOrderByMovementDateAsc(product);
	}
}
