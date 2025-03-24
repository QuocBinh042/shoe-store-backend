package com.shoestore.Server.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemDTO {
    private int cartItemID;

    @NotNull(message = "Cart không được để trống")
    private CartDTO cart;

    @NotNull(message = "Chi tiết sản phẩm không được để trống")
    private ProductDetailDTO productDetail;

    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private int quantity;
}
