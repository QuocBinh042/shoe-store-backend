package com.shoestore.Server.dto.response;

public record CustomerMetricsResponse(
        Double retentionRate,
        Double avgLifetimeValue,
        Double repeatPurchaseRate
) {
}
