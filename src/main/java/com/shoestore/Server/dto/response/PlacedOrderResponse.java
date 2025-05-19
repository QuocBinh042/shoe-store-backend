package com.shoestore.Server.dto.response;

import com.shoestore.Server.dto.request.OrderDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PlacedOrderResponse {
    private OrderResponse orderResponse;
    private List<PlacedOrderDetailsResponse> orderDetailsResponse;
    private PaymentResponse paymentResponse;
}
