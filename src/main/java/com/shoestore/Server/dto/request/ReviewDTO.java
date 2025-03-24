package com.shoestore.Server.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private int reviewID;

    @NotNull(message = "User cannot be null")
    private UserDTO user;

    @NotNull(message = "Product cannot be null")
    private ProductDTO product;

    @NotNull(message = "Order detail cannot be null")
    private OrderDetailDTO orderDetail;

    @NotNull(message = "Rating cannot be null")
    @Min(value = 1, message = "Minimum rating is 1 star")
    @Max(value = 5, message = "Maximum rating is 5 stars")
    private Integer rating;

    @NotBlank(message = "Comment cannot be blank")
    private String comment;

    private LocalDateTime createdAt;
}
