package com.shoestore.Server.service;


import com.shoestore.Server.dto.request.ProductDetailDTO;
import com.shoestore.Server.dto.request.ProductDetailRequest;
import com.shoestore.Server.dto.response.ProductDetailsResponse;
import com.shoestore.Server.enums.Color;
import com.shoestore.Server.enums.Size;

import java.util.List;

public interface ProductDetailService {
    List<ProductDetailDTO> getByProductId(int productId);

    ProductDetailDTO save(ProductDetailDTO productDetail);

    ProductDetailDTO getProductDetailById(int id);

    ProductDetailDTO getProductDetailByProductIdAndColorAndSize(int productId, Color color, Size size);

    ProductDetailsResponse createProductDetail(int productId, ProductDetailRequest productDetailRequest);

    ProductDetailsResponse updateProductDetail(int productDetailId, ProductDetailRequest productDetailRequest);

}
