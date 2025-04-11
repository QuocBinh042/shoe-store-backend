package com.shoestore.Server.service;

import com.shoestore.Server.dto.request.OrderDetailDTO;
import com.shoestore.Server.dto.response.OrderDetailResponse;
import com.shoestore.Server.dto.response.OrderDetailsResponse;

import java.util.List;

public interface OrderDetailService {
     OrderDetailDTO save(OrderDetailDTO orderDetail);
     List<OrderDetailsResponse> getOrderDetailByOrderID(int orderID);
}
