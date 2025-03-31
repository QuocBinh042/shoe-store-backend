package com.shoestore.Server.service;

import com.shoestore.Server.dto.request.OrderDetailDTO;
import java.util.List;

public interface OrderDetailService {
     OrderDetailDTO save(OrderDetailDTO orderDetail);

     List<OrderDetailDTO> getProductDetailByOrderID(int orderID) ;
}
