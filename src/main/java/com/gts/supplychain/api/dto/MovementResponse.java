package com.gts.supplychain.api.dto;

import java.time.LocalDateTime;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MovementResponse {

	private ProductResponse product;

	private String fromLocation;

	private String toLocation;

	private LocalDateTime movementDate;

}
