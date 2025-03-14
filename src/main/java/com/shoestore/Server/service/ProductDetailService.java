package com.shoestore.Server.service;


import com.shoestore.Server.dto.request.ProductDetailDTO;
import com.shoestore.Server.entities.Color;
import com.shoestore.Server.entities.ProductDetail;
import com.shoestore.Server.entities.Size;
import com.shoestore.Server.repositories.ProductRepository;

import java.util.List;

public interface ProductDetailService {
    ProductDetailDTO addProductDetail(ProductDetailDTO productDetail);
    List<ProductDetailDTO> getByProductId(int productId);
    ProductDetailDTO save(ProductDetailDTO productDetail);
    ProductDetailDTO getProductDetailById(int id);
    ProductDetailDTO getProductDetailByProductIdAndColorAndSize(int productId, Color color, Size size);
}
