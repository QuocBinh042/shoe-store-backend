package com.shoestore.Server.service;

import com.shoestore.Server.dto.request.OrderDTO;
import com.shoestore.Server.dto.request.UpdateOrderStatusRequest;
import com.shoestore.Server.dto.response.*;
import org.apache.coyote.BadRequestException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    List<OrderDTO> getAllOrders();

    OrderDTO updateOrderStatus(int orderId, String status);

    OrderDTO getOrderById(int orderID);

    OrderDTO addOrder(OrderDTO order);

    List<PlacedOrderResponse> getOrderByByUser(int userId);

    OrderDTO getOrderByCode(String orderCode);

    int getOrderQuantityByUserId(int id);

    Double getTotalAmountByUserId(int id);

    long getTotalOrdersByDay();

    long getTotalOrdersByMonth();

    long getTotalOrdersByYear();

    long getTotalOrders();

    Double getTotalOrderAmount();

    Double getTotalOrderAmountByDay();

    Double getTotalOrderAmountByMonth();

    Double getTotalOrderAmountByYear();

    long getCompletedOrders();

    long getCompletedOrdersByDay();

    long getCompletedOrdersByMonth();

    long getCompletedOrdersByYear();

    long getCanceledOrders();

    long getCanceledOrdersByDay();

    long getCanceledOrdersByMonth();

    long getCanceledOrdersByYear();

    List<OrderDTO> searchOrders(String query);

    PaginationResponse<OrderDTO> getAllOrdersSorted(String sort, int page, int pageSize);

    PaginationResponse<OrderDTO> getAllOrdersPaged(int page, int pageSize);

    PaginationResponse<OrderDTO> getOrdersByDay(int page, int pageSize);

    PaginationResponse<OrderDTO> getOrdersByMonth(int page, int pageSize);

    PaginationResponse<OrderDTO> getOrdersByYear(int page, int pageSize);

    BigDecimal getRevenueFromPromotions();

    long countOrdersWithPromotions();

    PaginationResponse<OrderResponse> filterOrders(
            String status,
            String query,
            LocalDate from,
            LocalDate to,
            int page,
            int pageSize,
            String sort,
            String mode
    );

    OrderStatusHistoryResponse updateOrderStatus(int id, UpdateOrderStatusRequest request);
    List<OrderStatusHistoryResponse> getOrderHistory(int orderID);
}
