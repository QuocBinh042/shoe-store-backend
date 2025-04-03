package com.shoestore.Server.dto.response;
import com.shoestore.Server.dto.request.ProductDTO;
import com.shoestore.Server.dto.request.ProductDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderDetailsResponse {
    private ProductDetailDTO productDetail;
    private Integer quantity;
    private Double price;
    private String imageURL;
}
