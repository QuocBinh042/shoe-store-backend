package com.shoestore.Server.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class KpiItemResponse {
    private String key;
    private BigDecimal current;
    private BigDecimal previous;
    private BigDecimal changePercent;
}
