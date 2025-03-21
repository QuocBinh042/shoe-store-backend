package com.shoestore.Server.service;

import com.shoestore.Server.dto.request.BrandDTO;

import java.util.List;

public interface BrandService {
    BrandDTO getBrandById(int id);
    List<BrandDTO> getAllBrands();
}
