package com.shoestore.Server.dto.response;

import com.shoestore.Server.dto.request.ProductDetailDTO;
import lombok.Data;

import java.util.List;
@Data
public class ProductSearchResponse {
    private int productID;
    private String productName;
    private List<String> imageURL;
    private String description;
    private double price;
    private String status;
    private List<ProductDetailDTO> productDetails;
    private double discountPrice;
    private double rating;
}
