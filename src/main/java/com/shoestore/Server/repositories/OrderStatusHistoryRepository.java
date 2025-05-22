package com.shoestore.Server.repositories;

import com.shoestore.Server.entities.OrderStatusHistory;
import com.shoestore.Server.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Long> {
    List<OrderStatusHistory> findByOrderOrderIDOrderByCreatedAtAsc(int orderID);
    List<OrderStatusHistory> findByOrder_OrderIDAndStatus(int order_orderID, OrderStatus status);
}