package com.shoestore.Server.service.impl;
import com.shoestore.Server.dto.request.BrandDTO;
import com.shoestore.Server.dto.request.CategoryDTO;
import com.shoestore.Server.dto.request.SupplierDTO;
import com.shoestore.Server.entities.Brand;
import com.shoestore.Server.entities.Category;
import com.shoestore.Server.mapper.BrandMapper;
import com.shoestore.Server.repositories.BrandRepository;
import com.shoestore.Server.service.BrandService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;
    public BrandServiceImpl(BrandRepository brandRepository, BrandMapper brandMapper) {
        this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
    }
    @Override
    public BrandDTO getBrandById(int id) {
        Brand brand=brandRepository.findByBrandID(id);
        return brandMapper.toDto(brand);
    }
    @Override
    public List<BrandDTO> getAllBrands() {
        return brandRepository.findAll().stream()
                .map(brandMapper::toDto)
                .collect(Collectors.toList());
    }
}
