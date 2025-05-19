package com.shoestore.Server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockAlertResponse {
    private int productId;
    private String name;
    private int stock;
    private String image;
}

