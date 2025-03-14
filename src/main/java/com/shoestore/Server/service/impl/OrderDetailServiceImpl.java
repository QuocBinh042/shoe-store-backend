package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.request.OrderDetailDTO;
import com.shoestore.Server.entities.Order;
import com.shoestore.Server.entities.OrderDetail;
import com.shoestore.Server.entities.ProductDetail;
import com.shoestore.Server.mapper.OrderDetailMapper;
import com.shoestore.Server.repositories.OrderDetailRepository;
import com.shoestore.Server.repositories.OrderRepository;
import com.shoestore.Server.repositories.ProductDetailRepository;
import com.shoestore.Server.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderDetailMapper orderDetailMapper;
    private final OrderRepository orderRepository;
    private final ProductDetailRepository productDetailRepository;

    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository, OrderDetailMapper orderDetailMapper,
                                  OrderRepository orderRepository, ProductDetailRepository productDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.orderDetailMapper = orderDetailMapper;
        this.orderRepository = orderRepository;
        this.productDetailRepository = productDetailRepository;
    }

    @Override
    public OrderDetailDTO save(OrderDetailDTO orderDetailDTO) {
        log.info("Saving order detail for Order ID: {}", orderDetailDTO.getOrder().getOrderID());

        Order order = orderRepository.findById(orderDetailDTO.getOrder().getOrderID())
                .orElseThrow(() -> {
                    log.error("Order not found with ID: {}", orderDetailDTO.getOrder().getOrderID());
                    return new RuntimeException("Order not found");
                });

        ProductDetail productDetail = productDetailRepository.findById(orderDetailDTO.getProductDetail().getProductDetailID())
                .orElseThrow(() -> {
                    log.error("Product detail not found with ID: {}", orderDetailDTO.getProductDetail().getProductDetailID());
                    return new RuntimeException("Product detail not found");
                });

        OrderDetail orderDetail = orderDetailMapper.toEntity(orderDetailDTO);
        orderDetail.setOrder(order);
        orderDetail.setProductDetail(productDetail);

        OrderDetail savedOrderDetail = orderDetailRepository.save(orderDetail);
        log.info("Order detail saved successfully with ID: {}", savedOrderDetail.getOrderDetailID());

        return orderDetailMapper.toDto(savedOrderDetail);
    }

    @Override
    public List<OrderDetailDTO> getProductDetailByOrderID(int orderID) {
        log.info("Fetching order details for Order ID: {}", orderID);

        List<OrderDetailDTO> orderDetails = orderDetailRepository.findByOrder_OrderID(orderID)
                .stream()
                .map(orderDetailMapper::toDto)
                .collect(Collectors.toList());

        log.info("Found {} order details for Order ID: {}", orderDetails.size(), orderID);
        return orderDetails;
    }
}
