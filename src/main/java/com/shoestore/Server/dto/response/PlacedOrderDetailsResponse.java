package com.shoestore.Server.dto.response;
import com.shoestore.Server.dto.request.ProductDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlacedOrderDetailsResponse {
    private ProductDetailDTO productDetail;
    private Integer quantity;
    private Double price;
    private String imageURL;
}
