package com.ecommerce.inventory_service.repository;

import com.ecommerce.inventory_service.model.Inventary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventary, Long> {
    Optional<Inventary> findByProductId(Long productId);
}
