package com.gts.supplychain.spec.product.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ProductResponse {

	private String productId;

	private String type;

	private LocalDateTime manufacturingDate;

	private String origin;

}
