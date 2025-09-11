package com.gts.supplychain.api.movement;


import java.util.List;

import org.springframework.http.MediaType;
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

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MovementResponse> recordMovement(@PathVariable String productId, @RequestBody MovementCreateRequest request) {
		log.info("start recording movement for productId: {}, request: {}", productId, request);
		Movement movement = movementService.recordMovement(productId, request);
		log.info("ens recording movement for productId: {}, movementId: {}", productId, movement.getId());
		return ResponseEntity.ok(mapper.toMovementResponse(movement));
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MovementResponse>> getMovements(@PathVariable String productId) {
		log.info("start get movements for productId: {}", productId);
		List<Movement> movements = movementService.getMovements(productId);
		log.info("ens get movements for productId: {} , count: {}", productId, movements.size());
		return ResponseEntity.ok(mapper.toListOfMovementResponse(movements));
	}
}
