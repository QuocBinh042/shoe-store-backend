package com.shoestore.Server.service;

import com.shoestore.Server.dto.response.PaginationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaginationService {
    <T> PaginationResponse<T> paginate(List<T> items, int page, int pageSize);

    <T> PaginationResponse<T> paginate(Page<T> page);

    Pageable createPageable(int page, int pageSize);
}
