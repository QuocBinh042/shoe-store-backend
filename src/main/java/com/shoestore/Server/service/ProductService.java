package com.shoestore.Server.service;

import com.shoestore.Server.dto.request.ProductDTO;
import com.shoestore.Server.dto.response.FeaturedProductResponse;
import com.shoestore.Server.dto.response.PaginationResponse;
import com.shoestore.Server.dto.response.SearchProductResponse;

import java.util.List;

public interface ProductService {
    PaginationResponse<SearchProductResponse> getAllProducts(int page, int pageSize);
    ProductDTO getProductByProductDetailsId(int id);

    ProductDTO getProductById(int id);

    //Search
    PaginationResponse<SearchProductResponse> getFilteredProducts(List<Integer> categoryIds, List<Integer> brandIds, List<String> colors, List<String> sizes,
                                                                  String keyword, Double minPrice, Double maxPrice, String sortBy, int page, int pageSize);



    PaginationResponse<ProductDTO> searchProducts(String status,
                                                         List<Integer> categoryIds,
                                                         List<Integer> brandIds,
                                                         List<Integer> supplierIds,
                                                         String searchText,
                                                         String stock,
                                                         int page,
                                                         int pageSize);
    ProductDTO createProduct(ProductDTO productDTO);
    
    ProductDTO updateProduct(int id, ProductDTO productDTO);

    boolean deleteProduct(int id);
    
    double getAverageRating(int id);
    List<SearchProductResponse> getRelatedProducts(int productId, int categoryId, int brandId);
    List<FeaturedProductResponse> getBestSellingProduct();
    List<FeaturedProductResponse> getNewArrivals();
}
