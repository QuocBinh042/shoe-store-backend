package com.shoestore.Server.service;

import com.shoestore.Server.dto.request.OrderDTO;
import com.shoestore.Server.dto.response.OrderResponse;
import com.shoestore.Server.entities.Order;
import com.shoestore.Server.entities.OrderDetail;
import com.shoestore.Server.entities.Product;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface OrderService {
    List<OrderDTO> getAll();

    OrderDTO updateOrderStatus(int orderId, String status);

    OrderDTO getOrderById(int orderID);

    OrderDTO addOrder(OrderDTO order);

    List<OrderResponse> getOrderByByUser(int userId);

    OrderDTO getOrderByCode(String orderCode);

    int getOrderQuantityByUserId(int id);

    Double getTotalAmountByUserId(int id);
}
