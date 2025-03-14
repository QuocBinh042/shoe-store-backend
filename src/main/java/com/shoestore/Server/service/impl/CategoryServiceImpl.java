package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.request.CategoryDTO;
import com.shoestore.Server.entities.Category;
import com.shoestore.Server.mapper.CategoryMapper;
import com.shoestore.Server.repositories.CategoryRepository;
import com.shoestore.Server.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryDTO getCategory(int id) {
        log.info("Fetching category with ID: {}", id);
        Category category = categoryRepository.findByCategoryID(id);
        if (category == null) {
            log.warn("Category not found with ID: {}", id);
            return null;
        }
        return categoryMapper.toDto(category);
    }

    @Override
    public List<CategoryDTO> getAllCategory() {
        log.info("Fetching all categories from database.");
        List<CategoryDTO> categories = categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
        log.info("Found {} categories.", categories.size());
        return categories;
    }
}
