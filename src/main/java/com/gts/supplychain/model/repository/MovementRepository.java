package com.gts.supplychain.model.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.gts.supplychain.model.entity.Movement;
import com.gts.supplychain.model.entity.Product;

public interface MovementRepository extends JpaRepository<Movement, Long> {

	List<Movement> findByProductOrderByMovementDateAsc(Product product);

}
