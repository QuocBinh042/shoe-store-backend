package com.shoestore.Server.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class KpiResponse {
    private List<KpiItemResponse> items;
}
