package com.shoestore.Server.service;

import com.shoestore.Server.dto.request.OrderDetailDTO;
import com.shoestore.Server.dto.request.UpdateOrderDetailRequest;
import com.shoestore.Server.dto.response.PlacedOrderDetailsResponse;
import com.shoestore.Server.entities.OrderDetail;

import java.util.List;

public interface OrderDetailService {
     OrderDetailDTO save(OrderDetailDTO orderDetail);
     List<PlacedOrderDetailsResponse> getOrderDetailByOrderID(int orderID);
     PlacedOrderDetailsResponse mapToPlacedOrderDetailsResponse(OrderDetail orderDetail);
     OrderDetail updateOrderDetail(int id, UpdateOrderDetailRequest request);

}
