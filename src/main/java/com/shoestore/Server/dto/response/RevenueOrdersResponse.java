package com.shoestore.Server.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class RevenueOrdersResponse {
    private List<RevenueSeriesResponse> revenueSeries;
    private List<OrderStatusCountResponse> orderStatus;
}
