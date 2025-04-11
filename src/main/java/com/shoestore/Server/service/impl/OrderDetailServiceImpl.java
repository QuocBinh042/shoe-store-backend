package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.request.OrderDetailDTO;
import com.shoestore.Server.dto.response.OrderDetailsResponse;
import com.shoestore.Server.entities.Order;
import com.shoestore.Server.entities.OrderDetail;
import com.shoestore.Server.entities.ProductDetail;
import com.shoestore.Server.mapper.OrderDetailMapper;
import com.shoestore.Server.mapper.OrderMapper;
import com.shoestore.Server.mapper.ProductDetailMapper;
import com.shoestore.Server.repositories.OrderDetailRepository;
import com.shoestore.Server.repositories.OrderRepository;
import com.shoestore.Server.repositories.ProductDetailRepository;
import com.shoestore.Server.service.OrderDetailService;
import com.shoestore.Server.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderDetailMapper orderDetailMapper;
    private final OrderRepository orderRepository;
    private final ProductDetailRepository productDetailRepository;
    private final ProductDetailMapper productDetailMapper;
    private final ProductService productService;
    private final OrderMapper orderMapper;

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

        int quantityToBuy = orderDetailDTO.getQuantity();

        if (productDetail.getStockQuantity() < quantityToBuy) {
            log.error("Not enough stock for productDetail ID: {}. Available: {}, Requested: {}",
                    productDetail.getProductDetailID(), productDetail.getStockQuantity(), quantityToBuy);
            throw new RuntimeException("Not enough stock for product");
        }
        productDetail.setStockQuantity(productDetail.getStockQuantity() - quantityToBuy);
        productDetailRepository.save(productDetail);

        OrderDetail orderDetail = orderDetailMapper.toEntity(orderDetailDTO);
        orderDetail.setOrder(order);
        orderDetail.setProductDetail(productDetail);

        OrderDetail savedOrderDetail = orderDetailRepository.save(orderDetail);
        log.info("Order detail saved successfully with ID: {}", savedOrderDetail.getOrderDetailID());

        return orderDetailMapper.toDto(savedOrderDetail);
    }
    @Override
    public List<OrderDetailsResponse> getOrderDetailByOrderID(int orderID) {
        List<OrderDetailsResponse> responses = orderDetailRepository.findByOrder_OrderID(orderID)
                .stream()
                .map(orderDetail -> {
                    var productDetailDTO = productDetailMapper.toDto(orderDetail.getProductDetail());
//                    var product = productService.getProductByProductDetailsId(productDetailDTO.getProductDetailID());
                    log.info("Product image: {}", productDetailDTO.getProductDetailID());
                    return new OrderDetailsResponse(
                            productDetailDTO,
                            orderDetail.getQuantity(),
                            orderDetail.getPrice(),
                            "OK"
                    );
                })
                .collect(Collectors.toList());

        log.info("Found {} order details for Order ID: {}", responses.size(), orderID);
        return responses;
    }
}
