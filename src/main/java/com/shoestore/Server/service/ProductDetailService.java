package com.shoestore.Server.service;


import com.shoestore.Server.dto.request.ProductDetailDTO;
import com.shoestore.Server.enums.Color;
import com.shoestore.Server.enums.Size;

import java.util.List;

public interface ProductDetailService {
    ProductDetailDTO addProductDetail(ProductDetailDTO productDetail);
    List<ProductDetailDTO> getByProductId(int productId);
    ProductDetailDTO save(ProductDetailDTO productDetail);
    ProductDetailDTO getProductDetailById(int id);
    ProductDetailDTO getProductDetailByProductIdAndColorAndSize(int productId, Color color, Size size);
}
