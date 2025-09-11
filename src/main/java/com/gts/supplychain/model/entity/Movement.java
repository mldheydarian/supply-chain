package com.gts.supplychain.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "movements",
		indexes = {
		@Index(name = "idx_movement_product", columnList = "product_id"),
		@Index(name = "idx_movement_date", columnList = "movement_date")
		},
		uniqueConstraints = {@UniqueConstraint(name = "uk_product_movement",
		columnNames = {"product_id", "movement_date", "from_location", "to_location"})})
public class Movement extends Auditable implements Serializable {

    @NotBlank
    private String fromLocation;

    @NotBlank
    private String toLocation;

    @NotNull
    private LocalDateTime movementDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

	public void setProduct(Product product) {
		this.product = product;
		if (!product.getMovements().contains(this)) {
			product.getMovements().add(this);
		}
	}

}
