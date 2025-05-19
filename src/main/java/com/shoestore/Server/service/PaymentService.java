package com.shoestore.Server.service;

import com.shoestore.Server.dto.request.PaymentDTO;
import com.shoestore.Server.dto.response.PaymentResponse;
import com.shoestore.Server.dto.response.PaymentUrlResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface PaymentService {
    PaymentDTO addPayment(PaymentDTO payment);
    PaymentDTO getPaymentById(int id);
    PaymentResponse getPaymentByOrderId(int id);
    List<PaymentDTO> getAll();
    void updateStatus(int orderId,String status);
    PaymentUrlResponse createVnPayPayment(HttpServletRequest request);
}
