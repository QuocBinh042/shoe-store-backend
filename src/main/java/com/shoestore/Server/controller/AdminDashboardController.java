package com.shoestore.Server.controller;

import com.shoestore.Server.dto.response.KpiResponse;
import com.shoestore.Server.dto.response.RevenueOrdersResponse;
import com.shoestore.Server.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
