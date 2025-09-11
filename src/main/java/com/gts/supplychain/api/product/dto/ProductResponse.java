package com.gts.supplychain.api.product.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ProductResponse {

	private String productId;

	private String type;

	private LocalDateTime manufacturingDate;

	private String origin;

}
