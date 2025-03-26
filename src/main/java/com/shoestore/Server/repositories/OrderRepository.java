package com.shoestore.Server.repositories;

import com.shoestore.Server.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
  List<Order> findByUser_UserID(int userID);
  Order findByCode(String code);
  @Query("SELECT COUNT(o) FROM Order o WHERE o.user.userID = :userId")
  int countOrdersByUserId(int userId);
  @Query("SELECT SUM(o.total) FROM Order o WHERE o.user.userID = :userId")
  Double sumTotalAmountByUserId(int userId);
  @Query("SELECT COUNT(o) FROM Order o WHERE o.user.userID = :userId AND o.status = 'DELIVERED'")
  int countDeliveredOrdersByUserId(@Param("userId") int userId);
  @Query("SELECT COALESCE(SUM(o.total), 0) FROM Order o WHERE o.user.userID = :userId")
  Double calculateTotalAmountByUserId(@Param("userId") int userId);
}
