package com.shoestore.Server.dto.response;

import lombok.Data;

@Data
public class KpiResponse {
    private double totalRevenue;
    private long totalOrders;
    private double avgOrderValue;
    private long newCustomers;
}
