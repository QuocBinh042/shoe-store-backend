package com.shoestore.Server.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class PaginationResponse<T> {
    private List<T> items;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;
}