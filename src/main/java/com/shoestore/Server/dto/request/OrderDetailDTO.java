package com.shoestore.Server.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderDetailDTO {
    private int orderDetailID;

    @NotNull(message = "Order cannot be null")
    private OrderDTO order;

    @NotNull(message = "Product details cannot be null")
    private ProductDetailDTO productDetail;

    private ProductDetailDTO giftProductDetail;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;

    @NotNull(message = "Price cannot be null")
    @Min(value = 0, message = "Price cannot be negative")
    private Double price;

    private PromotionDTO promotion;

    private Integer giftedQuantity;

//    private Double promoDiscount;
}
