package com.shoestore.Server.dto.response;

import com.shoestore.Server.dto.request.ProductDetailRequest;
import com.shoestore.Server.enums.ProductStatus;
import lombok.Data;

import java.util.List;
@Data
public class ProductResponse {
    private int productID;
    private String productName;
    private List<String> imageURL;
    private String description;
    private double price;
    private ProductStatus status;
    private int brandID;
    private int categoryID;
    private int supplierID;
    private List<ProductDetailRequest> productDetails;
    private PromotionResponse promotion;
}
