  package com.shoestore.Server.repositories;

  import com.shoestore.Server.entities.Order;
  import org.springframework.data.domain.Page;
  import org.springframework.data.domain.Pageable;
  import org.springframework.data.jpa.repository.JpaRepository;
  import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
  import org.springframework.data.jpa.repository.Query;
  import org.springframework.data.repository.query.Param;

  import java.math.BigDecimal;
  import java.time.LocalDate;
  import java.util.List;

  public interface OrderRepository extends JpaRepository<Order, Integer>, JpaSpecificationExecutor<Order> {
    List<Order> findByUser_UserID(int userID);
    Order findByCode(String code);
    @Query("SELECT COUNT(o) FROM Order o WHERE o.user.userID = :userId")
    int countOrdersByUserId(int userId);
    @Query("SELECT SUM(o.total) FROM Order o WHERE o.user.userID = :userId")
    Double sumTotalAmountByUserId(int userId);
    @Query("SELECT COUNT(o) FROM Order o WHERE o.user.userID = :userId AND o.status = 'DELIVERED'")
    int countDeliveredOrdersByUserId(@Param("userId") int userId);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderDate = :date")
    long countByOrderDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(o) FROM Order o WHERE FUNCTION('MONTH', o.orderDate) = :month AND FUNCTION('YEAR', o.orderDate) = :year")
    long countByMonthAndYear(@Param("month") int month, @Param("year") int year);

    @Query("SELECT COUNT(o) FROM Order o WHERE FUNCTION('YEAR', o.orderDate) = :year")
    long countByYear(@Param("year") int year);

    @Query("SELECT COALESCE(SUM(o.total), 0) FROM Order o")
    Double sumTotalOrderAmount();

    @Query("SELECT COALESCE(SUM(o.total), 0) FROM Order o WHERE o.orderDate = :today")
    Double sumTotalOrderAmountByDay(@Param("today") LocalDate today);

    @Query("SELECT COALESCE(SUM(o.total), 0) FROM Order o WHERE FUNCTION('MONTH', o.orderDate) = :month AND FUNCTION('YEAR', o.orderDate) = :year")
    Double sumTotalOrderAmountByMonth(@Param("month") int month, @Param("year") int year);

    @Query("SELECT COALESCE(SUM(o.total), 0) FROM Order o WHERE FUNCTION('YEAR', o.orderDate) = :year")
    Double sumTotalOrderAmountByYear(@Param("year") int year);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'COMPLETED'")
    long countCompletedOrders();

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'COMPLETED' AND o.orderDate = :today")
    long countCompletedOrdersByDay(@Param("today") LocalDate today);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'COMPLETED' AND FUNCTION('MONTH', o.orderDate) = :month AND FUNCTION('YEAR', o.orderDate) = :year")
    long countCompletedOrdersByMonth(@Param("month") int month, @Param("year") int year);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'COMPLETED' AND FUNCTION('YEAR', o.orderDate) = :year")
    long countCompletedOrdersByYear(@Param("year") int year);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'CANCELED'")
    long countCanceledOrders();

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'CANCELED' AND o.orderDate = :today")
    long countCanceledOrdersByDay(@Param("today") LocalDate today);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'CANCELED' AND FUNCTION('MONTH', o.orderDate) = :month AND FUNCTION('YEAR', o.orderDate) = :year")
    long countCanceledOrdersByMonth(@Param("month") int month, @Param("year") int year);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'CANCELED' AND FUNCTION('YEAR', o.orderDate) = :year")
    long countCanceledOrdersByYear(@Param("year") int year);

    @Query("SELECT o FROM Order o WHERE " +
            "CAST(o.orderID as string) LIKE CONCAT('%', :query, '%') " +
            "OR LOWER(o.user.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Order> searchOrders(@Param("query") String query);


    @Query("SELECT o FROM Order o WHERE o.orderDate = :day")
    Page<Order> findByOrderDate(@Param("day") LocalDate day, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE FUNCTION('MONTH', o.orderDate) = :month AND FUNCTION('YEAR', o.orderDate) = :year")
    Page<Order> findByMonthAndYear(@Param("month") int month, @Param("year") int year, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE FUNCTION('YEAR', o.orderDate) = :year")
    Page<Order> findByYear(@Param("year") int year, Pageable pageable);

    @Query("SELECT SUM(o.total) FROM Order o " +
            "WHERE EXISTS (SELECT od FROM OrderDetail od " +
            "JOIN od.productDetail pd " +
            "JOIN pd.product p " +
            "WHERE od.order = o AND p.promotion IS NOT NULL)")
    BigDecimal getRevenueFromPromotions();

    @Query("SELECT COUNT(DISTINCT o) FROM Order o " +
            "WHERE EXISTS (SELECT od FROM OrderDetail od " +
            "JOIN od.productDetail pd " +
            "JOIN pd.product p " +
            "WHERE od.order = o AND p.promotion IS NOT NULL)")
    long countOrdersWithPromotions();

    @Query("SELECT SUM(o.total) FROM Order o")
    Double sumTotalAmount();
  }
