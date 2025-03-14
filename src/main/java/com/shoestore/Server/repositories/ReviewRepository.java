package com.shoestore.Server.repositories;

import com.shoestore.Server.entities.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query("SELECT r FROM Review r " +
            "JOIN r.productDetail pd " +
            "JOIN pd.product p " +
            "WHERE p.productID = :productID")
    List<Review> findReviewsByProductID(@Param("productID") int productID);
}
