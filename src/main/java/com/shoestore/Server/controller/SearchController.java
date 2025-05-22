package com.shoestore.Server.controller;

import com.shoestore.Server.dto.response.PaginationResponse;
import com.shoestore.Server.dto.response.SearchProductResponse;
import com.shoestore.Server.service.BrandService;
import com.shoestore.Server.service.CategoryService;
import com.shoestore.Server.service.ProductService;
import com.shoestore.Server.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final SupplierService supplierService;

    @GetMapping("/show-filtered")
    public ResponseEntity<Map<String, Object>> getFilterOptions() {
        Map<String, Object> response = new HashMap<>();
        response.put("categories", categoryService.getAllCategories());
        response.put("brands", brandService.getAllBrands());
        response.put("suppliers", supplierService.getAllSuppliers());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all-products")
    public ResponseEntity<?> getAllProducts(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "12") int pageSize) {

        PaginationResponse<SearchProductResponse> paginatedProducts = productService.getAllProducts(page, pageSize);
        return ResponseEntity.ok(paginatedProducts);
    }


    @GetMapping("/filtered")
    public ResponseEntity<?> getFilteredProducts(
            @RequestParam(required = false) List<Integer> categoryIds,
            @RequestParam(required = false) List<Integer> brandIds,
            @RequestParam(required = false) List<String> colors,
            @RequestParam(required = false) List<String> sizes,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "12") int pageSize) {
        PaginationResponse<SearchProductResponse> paginatedProducts = productService.getFilteredProducts(
                categoryIds, brandIds, colors, sizes, keyword, minPrice, maxPrice, sortBy, page, pageSize);
        return ResponseEntity.ok(paginatedProducts);
    }

}
