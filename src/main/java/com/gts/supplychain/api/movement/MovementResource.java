package com.gts.supplychain.api.movement;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gts.supplychain.api.movement.dto.MovementResponse;
import com.gts.supplychain.api.movement.dto.MovementCreateRequest;
import com.gts.supplychain.api.movement.mapper.MovementResourceMapper;
import com.gts.supplychain.model.entity.Movement;
import com.gts.supplychain.service.movement.MovementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/products/{productId}/movements")
public class MovementResource {

    private final MovementService movementService;

	private final MovementResourceMapper mapper;

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
