package com.shoestore.Server.dto.response;

import com.shoestore.Server.dto.request.ProductDetailDTO;

import java.util.List;

import com.shoestore.Server.enums.ProductStatus;
import lombok.Data;

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
    private List<ProductDetailDTO> productDetails;
}
