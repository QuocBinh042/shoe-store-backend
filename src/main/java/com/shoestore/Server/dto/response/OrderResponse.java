package com.shoestore.Server.dto.response;

import com.shoestore.Server.dto.request.OrderDTO;
import lombok.Data;

import java.util.List;

@Data
public class OrderResponse {
    private OrderDTO orderDTO;
    private List<OrderDetailsResponse> orderDetailDTOs;
    private PaymentResponse paymentDTO;
}
