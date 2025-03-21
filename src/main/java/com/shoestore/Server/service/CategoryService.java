package com.shoestore.Server.service;

import com.shoestore.Server.dto.request.CategoryDTO;

import java.util.List;

public interface CategoryService {
    CategoryDTO getCategory(int id);
    List<CategoryDTO> getAllCategories();
}
