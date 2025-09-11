package com.gts.supplychain.api.movement;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gts.supplychain.api.movement.dto.MovementResponse;
import com.gts.supplychain.api.movement.dto.MovementCreateRequest;
import com.gts.supplychain.api.movement.mapper.MovementResourceMapper;
import com.gts.supplychain.model.entity.Movement;
import com.gts.supplychain.service.movement.MovementService;

@RestController
@RequestMapping("/api/products/{productId}/movements")
public class MovementResource {

    private final MovementService movementService;

	private final MovementResourceMapper mapper;

	public MovementResource(MovementService movementService, MovementResourceMapper mapper) {
		this.movementService = movementService;
		this.mapper = mapper;
	}


	@PostMapping
    public ResponseEntity<MovementResponse> recordMovement(@PathVariable String productId, @RequestBody MovementCreateRequest request) {
		Movement movement = movementService.recordMovement(productId, request);
		return ResponseEntity.ok(mapper.toMovementResponse(movement));
    }

    @GetMapping
    public ResponseEntity<List<MovementResponse>> getMovements(@PathVariable String productId) {
		List<Movement> movements = movementService.getMovements(productId);
		return ResponseEntity.ok(mapper.toListOfMovementResponse(movements));
    }
}
