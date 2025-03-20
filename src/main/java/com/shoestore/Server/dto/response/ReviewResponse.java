package com.shoestore.Server.dto.response;
import com.shoestore.Server.dto.request.OrderDetailDTO;
import com.shoestore.Server.dto.request.ProductDTO;
import com.shoestore.Server.dto.request.UserDTO;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class ReviewResponse {
    private int reviewID;
    private UserDTO user;
    private ProductDTO product;
    private OrderDetailDTO orderDetailDTO;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
    private String productDetailsSize;
    private String productDetailsColor;
}
