package com.shoestore.Server.dto.response;
import com.shoestore.Server.dto.request.ProductDetailDTO;
import com.shoestore.Server.dto.request.PromotionDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlacedOrderDetailsResponse {
    private int orderDetailId;
    private ProductDetailsResponse productDetails;
    private ProductDetailsResponse giftProductDetail;
    private Integer quantity;
    private Double price;
    private String giftProductName;
    private Integer giftedQuantity;
}
