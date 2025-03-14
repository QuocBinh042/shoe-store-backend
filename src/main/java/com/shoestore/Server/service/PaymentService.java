package com.shoestore.Server.service;

import com.shoestore.Server.dto.request.PaymentDTO;
import com.shoestore.Server.entities.Payment;

import java.util.List;

public interface PaymentService {
    PaymentDTO addPayment(PaymentDTO payment);
    PaymentDTO getPaymentById(int id);
    PaymentDTO getPaymentByOrderId(int id);
    List<PaymentDTO> getAll();
    void updateStatus(int orderId,String status);
}
