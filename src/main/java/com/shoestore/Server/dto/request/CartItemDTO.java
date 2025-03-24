package com.shoestore.Server.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemDTO {
    private int cartItemID;

    @NotNull(message = "Cart cannot be empty")
    private CartDTO cart;

    @NotNull(message = "ProductDetail cannot be empty")
    private ProductDetailDTO productDetail;

    @Min(value = 1, message = "Quantity must be greater than 0")
    private int quantity;
}
