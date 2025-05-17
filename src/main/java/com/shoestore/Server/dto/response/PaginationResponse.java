package com.shoestore.Server.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
@Builder
public class PaginationResponse<T> {
    private List<T> items;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;
}