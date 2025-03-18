package com.shoestore.Server.repositories;

import com.shoestore.Server.entities.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findReviewsByProduct_ProductID(int productID);
    Optional<Review> findByOrderDetail_OrderDetailID(int orderDetailID);
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.productID = :productId")
    Optional<Double> getAverageRatingByProductId(@Param("productId") int productId);

}
