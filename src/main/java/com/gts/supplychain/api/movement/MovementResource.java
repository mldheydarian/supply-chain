package com.gts.supplychain.api.movement;


import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gts.supplychain.spec.movement.request.MovementCreateRequest;
import com.gts.supplychain.spec.movement.response.MovementResponse;
import com.gts.supplychain.spec.common.response.PageableResponse;
import com.gts.supplychain.spec.common.request.PagingRequest;
import com.gts.supplychain.api.movement.mapper.MovementResourceMapper;
import com.gts.supplychain.exception.BusinessException;
import com.gts.supplychain.model.entity.Movement;
import com.gts.supplychain.service.movement.MovementService;
import jakarta.validation.Valid;
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
	public ResponseEntity<MovementResponse> recordMovement(@PathVariable String productId, @RequestBody MovementCreateRequest request) throws BusinessException {
		log.info("start recording movement for productId: {}, request: {}", productId, request);
		Movement movement = movementService.recordMovement(productId, request);
		log.info("ens recording movement for productId: {}, movementId: {}", productId, movement.getId());
		return ResponseEntity.ok(mapper.toMovementResponse(movement));
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PageableResponse<MovementResponse>> getMovements(
			@PathVariable String productId,@Valid PagingRequest pagingRequest) throws BusinessException {
		log.info("START get movements page for productId: {} page: {}",productId, pagingRequest);
		Page<Movement> movementsPage = movementService.getMovements(productId, pagingRequest.toPageable());
		PageableResponse<MovementResponse> response = new PageableResponse<>(
				movementsPage.map(mapper::toMovementResponse));
		log.info("END get movements for productId={}, returning {} movements in this page",
				productId, response.getContent().size());
		return ResponseEntity.ok(response);
	}


}
