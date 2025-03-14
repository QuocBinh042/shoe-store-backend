package com.shoestore.Server.dto.request;

import lombok.Data;

import java.util.List;
@Data
public class ProductDTO {
    private int productID;
    private String productName;
    private List<String> imageURL;
    private String description;
    private double price;
    private String status;
    private int brandID;
    private int categoryID;
    private int supplierID;
    private List<ProductDetailDTO> productDetails;

}

