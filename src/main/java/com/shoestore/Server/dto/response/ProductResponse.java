package com.shoestore.Server.dto.response;

import com.shoestore.Server.dto.request.ProductDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@AllArgsConstructor
@Data
public class ProductResponse {
    private List<ProductDetailDTO> productDetails;
    private String productName;
    private String categoryName;
    private String brandName;
    private String description;
    private double price;
    private List<String> imageURL;
    private double discountPrice;
}
