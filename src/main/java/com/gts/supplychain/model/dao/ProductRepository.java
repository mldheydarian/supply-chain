package com.gts.supplychain.model.dao;


import java.util.Optional;
import com.gts.supplychain.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductId(String productId);
}
