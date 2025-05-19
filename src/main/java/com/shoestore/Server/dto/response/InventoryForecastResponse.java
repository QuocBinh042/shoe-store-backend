package com.shoestore.Server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InventoryForecastResponse {
    private String productName;
    private int currentStock;
    private int totalSold;
    private double stockToSalesRatio;
    private String restockUrgency;
}