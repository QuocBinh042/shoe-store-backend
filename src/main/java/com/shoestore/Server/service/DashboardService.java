package com.shoestore.Server.service;

import com.shoestore.Server.dto.response.*;

import java.util.List;

public interface DashboardService {
    KpiResponse getKpiOverview(String timeFrame);

    RevenueOrdersResponse getRevenueAndOrders(String timeFrame);

    PaginationResponse<BestSellerResponse> getBestSellers(int page, int pageSize);

    PaginationResponse<StockAlertResponse> getStockAlerts(int threshold, int page, int pageSize);

    List<CustomerGrowthResponse> getCustomerGrowth(int year);

    List<CustomerRetentionResponse> getCustomerRetention(int year);

    CustomerMetricsResponse getCustomerMetrics(int year);

    PaginationResponse<InventoryForecastResponse> getInventoryForecast(int page, int pageSize);
}
