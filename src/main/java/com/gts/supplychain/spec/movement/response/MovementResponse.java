package com.gts.supplychain.spec.movement.response;

import java.time.LocalDateTime;


import com.gts.supplychain.spec.product.response.ProductResponse;
import lombok.Data;

@Data
public class MovementResponse {

	private ProductResponse product;

	private String fromLocation;

	private String toLocation;

	private LocalDateTime movementDate;

}
