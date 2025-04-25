package com.shoestore.Server.service;

import com.shoestore.Server.dto.response.*;


public interface DashboardService {
    KpiResponse getKpiOverview(String timeFrame);
    RevenueOrdersResponse getRevenueAndOrders(String timeFrame);
    PaginationResponse<BestSellerResponse> getBestSellers(int page, int pageSize);
    PaginationResponse<StockAlertResponse> getStockAlerts(int threshold, int page, int pageSize);
}
