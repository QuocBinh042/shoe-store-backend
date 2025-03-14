package com.shoestore.Server.dto.request;


import lombok.Data;

@Data
public class CartItemDTO {
    private int cartItemID;
    private CartDTO cart;
    private ProductDetailDTO productDetail;
    private int quantity;
}
