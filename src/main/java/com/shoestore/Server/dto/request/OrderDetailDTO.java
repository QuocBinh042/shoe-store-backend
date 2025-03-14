package com.shoestore.Server.dto.request;

import lombok.Data;

@Data

public class OrderDetailDTO {
    private int orderDetailID;
    private OrderDTO order;
    private ProductDetailDTO productDetail;
    private int quantity;
    private double price;

}
