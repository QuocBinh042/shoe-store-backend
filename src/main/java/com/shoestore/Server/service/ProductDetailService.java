package com.shoestore.Server.service;


import com.shoestore.Server.dto.request.ProductDetailDTO;
import com.shoestore.Server.dto.request.ProductDetailRequest;
import com.shoestore.Server.dto.response.OverviewProductResponse;
import com.shoestore.Server.dto.response.ProductDetailsResponse;
import com.shoestore.Server.enums.Color;
import com.shoestore.Server.enums.Size;

import java.util.List;

public interface ProductDetailService {
    List<ProductDetailsResponse> getByProductId(int productId);

    ProductDetailsResponse getProductDetailById(int id);

    OverviewProductResponse getProductOverviewById(int productId);

    ProductDetailsResponse createProductDetail(int productId, ProductDetailRequest productDetailRequest);

    ProductDetailsResponse updateProductDetail(int productDetailId, ProductDetailRequest productDetailRequest);

}
