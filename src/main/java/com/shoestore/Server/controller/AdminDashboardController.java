package com.shoestore.Server.controller;

import com.shoestore.Server.dto.response.*;
import com.shoestore.Server.service.DashboardService;
import com.shoestore.Server.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Year;
import java.util.List;

@RestController
@RequestMapping("/api/admin/dashboard")
@PreAuthorize("hasRole('SUPER_ADMIN')")
@RequiredArgsConstructor
public class AdminDashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/kpi")
    public ResponseEntity<KpiResponse> getKpiOverview(
            @RequestParam(defaultValue = "monthly") String timeFrame) {
        return ResponseEntity.ok(dashboardService.getKpiOverview(timeFrame));
    }

    @GetMapping("/revenue-orders")
    public ResponseEntity<RevenueOrdersResponse> getRevenueAndOrders(
            @RequestParam(defaultValue = "monthly") String timeFrame
    ) {
        var data = dashboardService.getRevenueAndOrders(timeFrame);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/best-sellers")
    public PaginationResponse<BestSellerResponse> getBestSellers(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) int page,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE) int pageSize
    ) {
        return dashboardService.getBestSellers(page, pageSize);
    }

    @GetMapping("/stock-alerts")
    public PaginationResponse<StockAlertResponse> getStockAlerts(
            @RequestParam(defaultValue = "10") int threshold,
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) int page,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE) int pageSize
    ) {
        return dashboardService.getStockAlerts(threshold, page, pageSize);
    }

    @GetMapping("/customers-growth")
    public List<CustomerGrowthResponse> getCustomerGrowth(
            @RequestParam(value = "year", required = false) Integer year
    ) {
        int y = (year != null) ? year : Year.now().getValue();
        return dashboardService.getCustomerGrowth(y);
    }

    @GetMapping("/customers-retention")
    public List<CustomerRetentionResponse> getCustomerRetention(
            @RequestParam(value = "year", required = false) Integer year
    ) {
        int y = (year != null) ? year : Year.now().getValue();
        return dashboardService.getCustomerRetention(y);
    }

    @GetMapping("/customers-metrics")
    public ResponseEntity<?> getCustomerMetrics(
            @RequestParam(value = "year", required = false) Integer year
    ) {
        int y = (year != null) ? year : Year.now().getValue();
        CustomerMetricsResponse data = dashboardService.getCustomerMetrics(y);
        return ResponseEntity.ok(data);
    }
}
