package com.shoestore.Server.service;

import com.shoestore.Server.dto.response.KpiResponse;

public interface DashboardService {
    KpiResponse getKpiOverview(String timeFrame);
}
