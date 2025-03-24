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

    @NotNull(message = "Người dùng không được để trống")
    private UserDTO user;

    @NotNull(message = "Sản phẩm không được để trống")
    private ProductDTO product;

    @NotNull(message = "Chi tiết đơn hàng không được để trống")
    private OrderDetailDTO orderDetail;

    @NotNull(message = "Đánh giá không được để trống")
    @Min(value = 1, message = "Đánh giá tối thiểu là 1 sao")
    @Max(value = 5, message = "Đánh giá tối đa là 5 sao")
    private Integer rating;

    @NotBlank(message = "Bình luận không được để trống")
    private String comment;

    private LocalDateTime createdAt;
}
