package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.request.ReceiptDTO;
import com.shoestore.Server.entities.Order;
import com.shoestore.Server.entities.Receipt;
import com.shoestore.Server.enums.OrderStatus;
import com.shoestore.Server.mapper.ReceiptMapper;
import com.shoestore.Server.repositories.OrderRepository;
import com.shoestore.Server.repositories.ReceiptRepository;
import com.shoestore.Server.service.ReceiptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final ReceiptMapper receiptMapper;
    private final OrderRepository orderRepository;

    @Autowired
    public ReceiptServiceImpl(ReceiptRepository receiptRepository, ReceiptMapper receiptMapper, OrderRepository orderRepository) {
        this.receiptRepository = receiptRepository;
        this.receiptMapper = receiptMapper;
        this.orderRepository = orderRepository;
    }

    @Override
    public ReceiptDTO addReceipt(ReceiptDTO receiptDTO) {
        Order order = orderRepository.findById(receiptDTO.getOrder().getOrderID())
                .orElseThrow(() -> {
                    log.error("Order not found with ID: {}", receiptDTO.getOrder().getOrderID());
                    return new RuntimeException("Order not found");
                });
        if (!order.getStatus().equals(OrderStatus.DELIVERED)) {
            log.error("Cannot create receipt for order {} because it is not delivered", order.getOrderID());
            throw new RuntimeException("Receipt can only be issued for delivered orders");
        }
        Receipt receipt = receiptMapper.toEntity(receiptDTO);
        receipt.setOrder(order);
        receipt.setReceiptDate(LocalDate.now());
        return receiptMapper.toDto(receiptRepository.save(receipt));
    }
}
