package com.shoestore.Server.dto.request;

import com.shoestore.Server.entities.ProductDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private int reviewID;
    private UserDTO user;
    private ProductDTO product;
    private OrderDetailDTO orderDetail;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
}

