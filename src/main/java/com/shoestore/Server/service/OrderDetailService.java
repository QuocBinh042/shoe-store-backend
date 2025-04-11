package com.shoestore.Server.service;

import com.shoestore.Server.dto.request.OrderDetailDTO;
import com.shoestore.Server.dto.response.PlacedOrderDetailsResponse;

import java.util.List;

public interface OrderDetailService {
     OrderDetailDTO save(OrderDetailDTO orderDetail);
     List<PlacedOrderDetailsResponse> getOrderDetailByOrderID(int orderID);
}
