package com.shoestore.Server.service;

import com.shoestore.Server.dto.request.OrderDetailDTO;
import com.shoestore.Server.entities.OrderDetail;
import com.shoestore.Server.entities.Product;

import java.time.LocalDate;
import java.util.List;

public interface OrderDetailService {
     OrderDetailDTO save(OrderDetailDTO orderDetail);

     List<OrderDetailDTO> getProductDetailByOrderID(int orderID) ;
}
