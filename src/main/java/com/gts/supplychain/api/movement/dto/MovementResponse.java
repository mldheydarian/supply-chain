package com.gts.supplychain.api.movement.dto;

import java.time.LocalDateTime;


import com.gts.supplychain.api.product.dto.ProductResponse;
import lombok.Data;

@Data
public class MovementResponse {

	private ProductResponse product;

	private String fromLocation;

	private String toLocation;

	private LocalDateTime movementDate;

}
