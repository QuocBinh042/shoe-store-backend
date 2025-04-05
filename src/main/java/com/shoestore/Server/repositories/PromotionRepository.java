package com.shoestore.Server.repositories;

import com.shoestore.Server.entities.Promotion;
import com.shoestore.Server.enums.PromotionStatus;
import com.shoestore.Server.enums.PromotionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer>, JpaSpecificationExecutor<Promotion> {
    @Query("SELECT p.promotion FROM Product p WHERE p.productID = :productId")
    Optional<Promotion> findPromotionByProductId(@Param("productId") int productId);

    long countByStatus(PromotionStatus status);

    List<Promotion> findByStatusAndStartDateBeforeAndEndDateAfter(
            PromotionStatus status, LocalDateTime startDate, LocalDateTime endDate);

}

