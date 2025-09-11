package com.gts.supplychain.api.product.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductCreateRequest {

	@NotBlank
	private String productId;

	@NotBlank
	private String type;

	@NotNull
	private LocalDateTime manufacturingDate;

	@NotBlank
	private String origin;


}
