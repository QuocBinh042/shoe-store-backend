package com.shoestore.Server.mapper;

import com.shoestore.Server.dto.request.ReviewDTO;
import com.shoestore.Server.dto.response.ReviewResponse;
import com.shoestore.Server.entities.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ProductDetailMapper.class, OrderMapper.class})
public interface ReviewMapper {

    @Mapping(source = "user", target = "user")
    @Mapping(source = "product", target = "product")
    @Mapping(source = "orderDetail", target = "orderDetail")
    ReviewDTO toDto(Review entity);

    @Mapping(source = "user", target = "user")
    @Mapping(source = "product", target = "product")
    @Mapping(source = "orderDetail", target = "orderDetail")
    Review toEntity(ReviewDTO dto);

    List<ReviewDTO> toDtoList(List<Review> entities);

    List<Review> toEntityList(List<ReviewDTO> dtos);
    List<ReviewResponse> toListReviewResponse(List<Review> reviews);
    ReviewResponse toReviewResponse(Review review);
}
