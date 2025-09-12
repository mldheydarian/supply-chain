package com.gts.supplychain.model.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.gts.supplychain.model.entity.Movement;
import com.gts.supplychain.model.entity.Product;

public interface MovementRepository extends JpaRepository<Movement, Long> {

	Page<Movement> findByProductOrderByMovementDateAsc(Product product, Pageable page);

}
