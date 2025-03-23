package com.shoestore.Server.service;

import com.shoestore.Server.dto.request.ProductDTO;
import com.shoestore.Server.dto.request.ProductDetailDTO;
import com.shoestore.Server.dto.response.PaginationResponse;
import com.shoestore.Server.dto.response.ProductSearchResponse;
import com.shoestore.Server.entities.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    PaginationResponse<ProductSearchResponse> getAllProduct(int page, int pageSize);
    List<ProductSearchResponse> getAllProductsNoPaging();
    ProductDTO getProductByProductDetailsId(int id);

    ProductDTO getProductById(int id);

    //Search
    PaginationResponse<ProductSearchResponse> getFilteredProducts(List<Integer> categoryIds, List<Integer> brandIds, List<String> colors, List<String> sizes,
                                         String keyword, Double minPrice, Double maxPrice, String sortBy, int page, int pageSize);
    List<ProductSearchResponse> getFilteredProductsNoPaging(List<Integer> categoryIds, List<Integer> brandIds, List<String> colors, List<String> sizes,
                                                 String keyword, Double minPrice, Double maxPrice, String sortBy);
    PaginationResponse<ProductDTO> searchProducts(String status, List<Integer> categoryIds, Integer minStock, Integer maxStock, int page, int pageSize);


    ProductDTO createProduct(ProductDTO productDTO);
    
    ProductDTO updateProduct(int id, ProductDTO productDTO);

    boolean deleteProduct(int id);
    
    double getAverageRating(int id);
    List<ProductDTO> getRelatedProducts(int productId, int categoryId,int brandId);
}
