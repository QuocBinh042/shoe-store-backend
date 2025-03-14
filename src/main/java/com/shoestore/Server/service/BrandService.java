package com.shoestore.Server.service;



import com.shoestore.Server.dto.request.BrandDTO;
import com.shoestore.Server.entities.Brand;

import java.util.List;

public interface BrandService {
    BrandDTO getBrandById(int id);
    List<BrandDTO> getAllBrand();
}
