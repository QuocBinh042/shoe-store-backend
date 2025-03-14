package com.shoestore.Server.repositories;

import com.shoestore.Server.entities.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
    @Query("SELECT p.promotion FROM Product p WHERE p.productID = :productId")
    Optional<Promotion> findPromotionByProductId(@Param("productId") int productId);
}

