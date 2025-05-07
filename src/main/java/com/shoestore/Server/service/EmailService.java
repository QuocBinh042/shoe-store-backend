package com.shoestore.Server.service;


import com.shoestore.Server.dto.request.OrderEmailRequest;
import com.shoestore.Server.enums.OrderStatus;

public interface EmailService {
    void sendOrderStatusEmail(int orderId, OrderStatus status);
    void sendOrderSuccessEmail(String to, String customerName, String orderCode);

}
