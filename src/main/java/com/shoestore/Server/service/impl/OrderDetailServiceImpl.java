package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.request.OrderDetailDTO;
import com.shoestore.Server.dto.response.PlacedOrderDetailsResponse;
import com.shoestore.Server.entities.*;
import com.shoestore.Server.mapper.OrderDetailMapper;
import com.shoestore.Server.mapper.ProductDetailMapper;
import com.shoestore.Server.repositories.*;
import com.shoestore.Server.service.OrderDetailService;
import jakarta.persistence.EntityNotFoundException;
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
    private final PromotionRepository promotionRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderDetailDTO save(OrderDetailDTO orderDetailDTO) {
        log.info("Saving order detail for Order ID: {}", orderDetailDTO.getOrder().getOrderID());
        OrderDetail orderDetail = orderDetailMapper.toEntity(orderDetailDTO);
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
        if (orderDetailDTO.getPromotion() != null) {
            Promotion promotion = promotionRepository.findById(orderDetailDTO.getPromotion().getPromotionID())
                    .orElseThrow(() -> {
                        log.error("Promotion not found with ID: {}", orderDetailDTO.getPromotion().getPromotionID());
                        return new RuntimeException("Promotion detail not found");
                    });
            orderDetail.setPromotion(promotion);
            if (promotion.getGiftProduct()!=null){
                ProductDetail giftProductDetail = productDetailRepository.findById(orderDetailDTO.getGiftProductDetail().getProductDetailID())
                        .orElseThrow(() -> {
                            log.error("Gift product detail not found with ID: {}", orderDetailDTO.getGiftProductDetail().getProductDetailID());
                            return new RuntimeException("Product detail not found");
                        });

                if (giftProductDetail.getStockQuantity() < 1) {
                    log.error("Not enough stock for gift productDetail ID: {}. Available: {}, Required: {}",
                            giftProductDetail.getProductDetailID(), giftProductDetail.getStockQuantity(), 1);
                    throw new RuntimeException("Not enough stock for gift product");
                }
                giftProductDetail.setStockQuantity(giftProductDetail.getStockQuantity() - 1);
                productDetailRepository.save(giftProductDetail);

                orderDetail.setGiftProductDetail(giftProductDetail);
                orderDetail.setGiftedQuantity(1);
            }

        }

        int quantityToBuy = orderDetailDTO.getQuantity();
        if (productDetail.getStockQuantity() < quantityToBuy) {
            log.error("Not enough stock for productDetail ID: {}. Available: {}, Requested: {}",
                    productDetail.getProductDetailID(), productDetail.getStockQuantity(), quantityToBuy);
            throw new RuntimeException("Not enough stock for product");
        }
        productDetail.setStockQuantity(productDetail.getStockQuantity() - quantityToBuy);
        productDetailRepository.save(productDetail);
        orderDetail.setOrder(order);
        orderDetail.setProductDetail(productDetail);

        OrderDetail savedOrderDetail = orderDetailRepository.save(orderDetail);
        log.info("Order detail saved successfully with ID: {}", savedOrderDetail.getOrderDetailID());
        return orderDetailMapper.toDto(savedOrderDetail);
    }

    @Override
    public List<PlacedOrderDetailsResponse> getOrderDetailByOrderID(int orderID) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrder_OrderID(orderID);

        List<PlacedOrderDetailsResponse> responses = orderDetails.stream()
                .map(this::mapToPlacedOrderDetailsResponse)
                .collect(Collectors.toList());

        log.info("Found {} order details for Order ID: {}", responses.size(), orderID);
        return responses;
    }
    @Override
    public PlacedOrderDetailsResponse mapToPlacedOrderDetailsResponse(OrderDetail orderDetail) {
        ProductDetail productDetail = productDetailRepository.findById(orderDetail.getProductDetail().getProductDetailID())
                .orElseThrow(() -> new EntityNotFoundException("Not found ProductDetail with id: " + orderDetail.getProductDetail().getProductDetailID()));
        Product product = productRepository.findProductByProductDetailId(productDetail.getProductDetailID());
        if (orderDetail.getGiftProductDetail()!=null) {
            ProductDetail giftProductDetail = productDetailRepository.findById(orderDetail.getGiftProductDetail().getProductDetailID())
                    .orElseThrow(() -> new EntityNotFoundException("Not found ProductDetail with id: " + orderDetail.getGiftedQuantity()));

            Product giftProduct = productRepository.findProductByProductDetailId(giftProductDetail.getProductDetailID());

            return new PlacedOrderDetailsResponse(
                    orderDetail.getOrderDetailID(),
                    productDetailMapper.toResponse(productDetail),
                    productDetailMapper.toResponse(giftProductDetail),
                    orderDetail.getQuantity(),
                    orderDetail.getPrice(),
                    giftProduct.getProductName(),
                    orderDetail.getGiftedQuantity()
            );
        }

        return new PlacedOrderDetailsResponse(
                orderDetail.getOrderDetailID(),
                productDetailMapper.toResponse(productDetail),
                null,
                orderDetail.getQuantity(),
                orderDetail.getPrice(),
                null,
                null
        );
    }

}
