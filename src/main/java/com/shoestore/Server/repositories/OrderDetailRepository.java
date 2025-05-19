package com.shoestore.Server.repositories;

import com.shoestore.Server.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findByOrder_OrderID(int id);

    @Query("SELECT COALESCE(SUM(od.quantity), 0) FROM OrderDetail od WHERE od.productDetail.product.productID = :productId")
    int getTotalSoldByProductId(int productId);
}
