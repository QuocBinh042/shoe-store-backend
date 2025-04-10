package com.shoestore.Server.service;

import com.shoestore.Server.dto.request.OrderDetailDTO;
import com.shoestore.Server.dto.response.OrderDetailResponse;

import java.util.List;

public interface OrderDetailService {
     OrderDetailDTO save(OrderDetailDTO orderDetail);

     List<OrderDetailDTO> getProductDetailByOrderID(int orderID);

     List<OrderDetailResponse> getOrderDetailByOrderID(int orderID);
}
