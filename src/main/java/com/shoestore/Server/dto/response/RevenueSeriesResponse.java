package com.shoestore.Server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevenueSeriesResponse {
    private String period;
    private double revenue;
    private long orders;
}
