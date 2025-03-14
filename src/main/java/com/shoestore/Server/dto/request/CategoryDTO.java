package com.shoestore.Server.dto.request;


import lombok.Data;

import java.util.List;
@Data
public class CategoryDTO {
    private int categoryID;
    private String name;
    private String description;
    private List<ProductDTO> products;

}

