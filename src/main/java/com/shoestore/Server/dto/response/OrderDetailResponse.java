package com.shoestore.Server.dto.response;

import com.shoestore.Server.dto.request.OrderDTO;
import lombok.Data;

@Data
public class OrderDetailResponse {
    private int orderDetailID;

    private OrderDTO order;

    private ProductDetailsResponse productDetail;

    private Integer quantity;

    private Double price;
}
