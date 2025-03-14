package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.request.OrderDTO;
import com.shoestore.Server.entities.Order;
import com.shoestore.Server.entities.User;
import com.shoestore.Server.entities.Voucher;
import com.shoestore.Server.mapper.OrderMapper;
import com.shoestore.Server.repositories.OrderRepository;
import com.shoestore.Server.repositories.UserRepository;
import com.shoestore.Server.repositories.VoucherRepository;
import com.shoestore.Server.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final VoucherRepository voucherRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper,
                            VoucherRepository voucherRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.voucherRepository = voucherRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<OrderDTO> getAll() {
        log.info("Fetching all orders...");
        List<OrderDTO> orders = orderRepository.findAll()
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
        log.info("Found {} orders", orders.size());
        return orders;
    }

    @Override
    public OrderDTO updateOrderStatus(int orderId, String status) {
        log.info("Updating status for Order ID: {} to {}", orderId, status);
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            log.warn("Order with ID {} not found", orderId);
            throw new IllegalArgumentException("Không tìm thấy đơn hàng với ID: " + orderId);
        }
        Order order = optionalOrder.get();
        order.setStatus(status);
        orderRepository.save(order);
        log.info("Updated Order ID {} status to {}", orderId, status);
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDTO getOrderById(int orderId) {
        log.info("Fetching order by ID: {}", orderId);
        return orderRepository.findById(orderId)
                .map(orderMapper::toDto)
                .orElseGet(() -> {
                    log.warn("Order with ID {} not found", orderId);
                    return null;
                });
    }

    @Override
    public OrderDTO addOrder(OrderDTO orderDTO) {
        log.info("Adding new order for User ID: {}", orderDTO.getUser().getUserID());
        Order order = orderMapper.toEntity(orderDTO);

        if (orderDTO.getVoucher() != null) {
            log.info("Applying Voucher ID: {}", orderDTO.getVoucher().getVoucherID());
            Voucher voucher = voucherRepository.findById(orderDTO.getVoucher().getVoucherID())
                    .orElseThrow(() -> {
                        log.error("Voucher not found with ID: {}", orderDTO.getVoucher().getVoucherID());
                        return new IllegalArgumentException("Voucher not found");
                    });
            order.setVoucher(voucher);
        }

        User user = userRepository.findById(orderDTO.getUser().getUserID())
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", orderDTO.getUser().getUserID());
                    return new IllegalArgumentException("User not found");
                });

        order.setUser(user);
        Order savedOrder = orderRepository.save(order);
        log.info("Order added successfully with ID: {}", savedOrder.getOrderID());

        return orderMapper.toDto(savedOrder);
    }

    @Override
    public List<OrderDTO> getOrderByByUser(int userId) {
        log.info("Fetching orders for User ID: {}", userId);
        List<OrderDTO> orders = orderRepository.findByUser_UserID(userId)
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
        log.info("Found {} orders for User ID: {}", orders.size(), userId);
        return orders;
    }

    @Override
    public OrderDTO getOrderByCode(String code) {
        log.info("Fetching order by code: {}", code);
        Order order = orderRepository.findByCode(code);
        if (order != null) {
            log.info("Order found with code: {}", code);
        } else {
            log.warn("No order found with code: {}", code);
        }
        return order != null ? orderMapper.toDto(order) : null;
    }

    @Override
    public int getOrderQuantityByUserId(int id) {
        log.info("Counting orders for User ID: {}", id);
        int count = orderRepository.countOrdersByUserId(id);
        log.info("User ID {} has {} orders", id, count);
        return count;
    }

    @Override
    public Double getTotalAmountByUserId(int id) {
        log.info("Calculating total order amount for User ID: {}", id);
        Double total = orderRepository.sumTotalAmountByUserId(id);
        log.info("Total order amount for User ID {}: {}", id, total != null ? total : 0.0);
        return total != null ? total : 0.0;
    }
}
