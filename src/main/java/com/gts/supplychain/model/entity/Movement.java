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
@Table(name = "movements")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
//		if (!product.getMovements().contains(this)) {
//			product.getMovements().add(this);
//		}
	}

}
