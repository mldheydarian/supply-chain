package com.gts.supplychain.model.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "products", indexes = { @Index(name = "idx_product_productId", columnList = "productId") },
		uniqueConstraints = { @UniqueConstraint(name = "uk_product_productId", columnNames = "productId") })
public class Product extends Auditable implements Serializable {

	@Serial
	private static final long serialVersionUID = 13244321423L;

	@NotBlank
	private String productId;

	@NotBlank
	private String type;

	@NotNull
	private LocalDateTime manufacturingDate;

	@NotBlank
	private String origin;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("movementDate ASC")
	private List<Movement> movements = new ArrayList<>();


	public void addMovement(Movement movement) {
		movements.add(movement);
		movement.setProduct(this);
	}
}
