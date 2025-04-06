package com.shoestore.Server.dto.request;

import com.shoestore.Server.enums.ProductStatus;
import lombok.Data;

import java.util.List;
@Data
public class ProductDTO {
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

}

