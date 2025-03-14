package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.response.PaginationResponse;
import com.shoestore.Server.service.PaginationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaginationServiceImpl implements PaginationService {

    @Override
    public  <T> PaginationResponse<T> paginate(List<T> items, int page, int pageSize) {
        int totalItems = items.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        if (page > totalPages) {
            page = totalPages;
        }

        int fromIndex = Math.min((page - 1) * pageSize, totalItems);
        int toIndex = Math.min(page * pageSize, totalItems);

        List<T> paginatedItems = items.subList(fromIndex, toIndex);

        return new PaginationResponse<>(paginatedItems, totalItems, totalPages, page, pageSize);
    }

    @Override
    public <T> PaginationResponse<T> paginate(Page<T> page) {
        return new PaginationResponse<>(page.getContent(), page.getTotalElements(), page.getTotalPages(), page.getNumber() + 1, page.getSize());
    }

    @Override
    public  Pageable createPageable(int page, int pageSize) {
        return PageRequest.of(page - 1, pageSize);
    }
}
