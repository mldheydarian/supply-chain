package com.gts.supplychain.spec.movement.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MovementCreateRequest {


	@NotBlank
	private String fromLocation;

	@NotBlank
	private String toLocation;

	@NotNull
	private LocalDateTime movementDate;

}
