package com.shoestore.Server.service;

import com.shoestore.Server.dto.response.KpiResponse;
import com.shoestore.Server.dto.response.RevenueOrdersResponse;

public interface DashboardService {
    KpiResponse getKpiOverview(String timeFrame);
    RevenueOrdersResponse getRevenueAndOrders(String timeFrame);
}
