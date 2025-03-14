package com.shoestore.Server.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class BrandDTO {
    private int brandID;
    private String name;
    private String image;
}