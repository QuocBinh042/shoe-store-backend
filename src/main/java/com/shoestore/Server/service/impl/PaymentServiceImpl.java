package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.request.PaymentDTO;
import com.shoestore.Server.entities.Order;
import com.shoestore.Server.entities.Payment;
import com.shoestore.Server.mapper.PaymentMapper;
import com.shoestore.Server.repositories.OrderRepository;
import com.shoestore.Server.repositories.PaymentRepository;
import com.shoestore.Server.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderRepository orderRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentMapper paymentMapper, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.orderRepository = orderRepository;
    }

    @Override
    public PaymentDTO addPayment(PaymentDTO paymentDTO) {
        log.info("Adding payment for Order ID: {}", paymentDTO.getOrder().getOrderID());

        Order order = orderRepository.findById(paymentDTO.getOrder().getOrderID())
                .orElseThrow(() -> {
                    log.error("Order not found with ID: {}", paymentDTO.getOrder().getOrderID());
                    return new RuntimeException("Order not found");
                });

        Payment payment = paymentMapper.toEntity(paymentDTO);
        payment.setOrder(order);
        Payment savedPayment = paymentRepository.save(payment);

        log.info("Payment added successfully with ID: {}", savedPayment.getPaymentID());
        return paymentMapper.toDto(savedPayment);
    }

    @Override
    public PaymentDTO getPaymentById(int id) {
        log.info("Fetching payment with ID: {}", id);

        return paymentRepository.findById(id)
                .map(paymentMapper::toDto)
                .orElseGet(() -> {
                    log.warn("Payment with ID {} not found", id);
                    return null;
                });
    }

    @Override
    public PaymentDTO getPaymentByOrderId(int id) {
        log.info("Fetching payment for Order ID: {}", id);

        Payment payment = paymentRepository.findPaymentByOrderId(id);
        if (payment != null) {
            log.info("Payment found for Order ID: {}", id);
        } else {
            log.warn("No payment found for Order ID: {}", id);
        }

        return payment != null ? paymentMapper.toDto(payment) : null;
    }

    @Override
    public List<PaymentDTO> getAll() {
        log.info("Fetching all payments...");
        List<PaymentDTO> payments = paymentRepository.findAll()
                .stream()
                .map(paymentMapper::toDto)
                .collect(Collectors.toList());
        log.info("Found {} payments", payments.size());
        return payments;
    }

    @Override
    public void updateStatus(int orderId, String status) {
        log.info("Updating payment status for Order ID: {} to {}", orderId, status);

        Payment payment = paymentRepository.findPaymentByOrderId(orderId);
        if (payment != null) {
            payment.setStatus(status);
            payment.setPaymentDate(LocalDate.now());
            paymentRepository.save(payment);
            log.info("Payment status updated for Order ID: {} to {}", orderId, status);
        } else {
            log.warn("No payment found for Order ID: {}", orderId);
        }
    }
}
