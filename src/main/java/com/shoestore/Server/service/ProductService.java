package com.shoestore.Server.service;

import com.shoestore.Server.dto.request.ProductDTO;
import com.shoestore.Server.dto.response.PaginationResponse;
import com.shoestore.Server.entities.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    PaginationResponse<ProductDTO> getAllProduct(int page, int pageSize);

    ProductDTO getProductByProductDetailsId(int id);

    ProductDTO getProductById(int id);

    //Search
    PaginationResponse<ProductDTO> getFilteredProducts(List<Integer> categoryIds, List<Integer> brandIds, List<String> colors, List<String> sizes,
                                         String keyword, Double minPrice, Double maxPrice, String sortBy, int page, int pageSize);
    double getAverageRating(int id);
    List<ProductDTO> getRelatedProducts(int productId, int categoryId,int brandId);
}
