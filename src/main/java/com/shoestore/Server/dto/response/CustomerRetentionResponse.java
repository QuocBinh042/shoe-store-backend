package com.shoestore.Server.dto.response;

public record CustomerRetentionResponse(
        String month,
        Double retentionRate
) {
}
