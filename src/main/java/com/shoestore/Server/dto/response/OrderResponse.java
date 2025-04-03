package com.shoestore.Server.dto.response;

import com.shoestore.Server.dto.request.OrderDTO;
import com.shoestore.Server.dto.request.OrderDetailDTO;
import com.shoestore.Server.dto.request.PaymentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@AllArgsConstructor
@Data
public class OrderResponse {
    private OrderDTO orderDTO;
    private List<OrderDetailsResponse> orderDetailDTOs;
    private PaymentResponse paymentDTO;
}
