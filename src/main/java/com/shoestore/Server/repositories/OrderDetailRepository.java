package com.shoestore.Server.repositories;


import com.shoestore.Server.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findByOrder_OrderID(int id);
    @Query("SELECT od FROM OrderDetail od WHERE od.productDetail.productDetailID = :productDetailID AND od.order.orderID = :orderID")
    List<OrderDetail> findByProductIDAndOrderID(@Param("productDetailID") int productID, @Param("orderID") int orderID);

    @Query("""
    SELECT p, SUM(od.quantity) AS totalQuantity
    FROM OrderDetail od
    JOIN od.productDetail pd
    JOIN pd.product p
    WHERE od.order.orderDate BETWEEN :startDate AND :endDate
    GROUP BY p
    ORDER BY totalQuantity DESC
    """)
    List<Object[]> findTopSellingProducts(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT od.productDetail.product.productID FROM OrderDetail od WHERE od.order.orderID = :orderID")
    List<Integer> findProductIDsByOrderID(@Param("orderID") int orderID);


}
