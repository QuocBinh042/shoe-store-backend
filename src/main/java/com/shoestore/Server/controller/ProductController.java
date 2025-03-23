package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.ProductDTO;
import com.shoestore.Server.dto.response.ApiStatusResponse;
import com.shoestore.Server.dto.response.PaginationResponse;
import com.shoestore.Server.dto.response.ProductSearchResponse;
import com.shoestore.Server.dto.response.RestResponse;
import com.shoestore.Server.entities.Product;
import com.shoestore.Server.repositories.ProductRepository;
import com.shoestore.Server.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable int id) {
        ProductDTO productDTO = productService.getProductById(id);
        return productDTO != null ? ResponseEntity.ok(productDTO) : ResponseEntity.notFound().build();
    }

    @GetMapping("/by-product-details-id/{id}")
    public ResponseEntity<ProductDTO> getProductsByProductDetails(@PathVariable int id) {
        ProductDTO productDTO = productService.getProductByProductDetailsId(id);
        return productDTO != null ? ResponseEntity.ok(productDTO) : ResponseEntity.notFound().build();
    }
    @GetMapping("/get-rating/{id}")
    public ResponseEntity<?> getProductRating(@PathVariable int id) {
        double avgRating = productService.getAverageRating(id);
        return ResponseEntity.ok(avgRating);
    }
    @GetMapping("/get-related/{id}")
    public ResponseEntity<?> getRelatedProducts(@PathVariable int id) {
        ProductDTO product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new RestResponse<>(HttpStatus.NOT_FOUND.value(), "Product not found", null, null));
        }

        List<ProductDTO> relatedProducts = productService.getRelatedProducts(id, product.getCategoryID(), product.getBrandID());
        return ResponseEntity.ok(relatedProducts);
    }



    @GetMapping("/search")
    public ResponseEntity<PaginationResponse<ProductDTO>> searchProducts(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) List<Integer> categoryIds,
            @RequestParam(required = false) List<Integer> brandIds,
            @RequestParam(required = false) List<Integer> supplierIds,
            @RequestParam(required = false) String searchText,
            @RequestParam(required = false) String stock,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        PaginationResponse<ProductDTO> products = productService.searchProducts(
                status, categoryIds, brandIds, supplierIds, searchText, stock, page, pageSize);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<RestResponse<Object>> createProduct(
            @Valid @RequestBody ProductDTO productDTO) {

        ProductDTO createdProduct = productService.createProduct(productDTO);
        RestResponse<Object> resp = new RestResponse<>();

        if (createdProduct != null) {
            resp.setStatusCode(ApiStatusResponse.CREATED.getCode());
            resp.setMessage(ApiStatusResponse.CREATED.getMessage());
            resp.setData(createdProduct);
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } else {
            resp.setStatusCode(ApiStatusResponse.NOT_FOUND.getCode());
            resp.setMessage("Product not created");
            resp.setData(null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> updateProduct(
            @PathVariable int id,
            @Valid @RequestBody ProductDTO productDTO) {

        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        RestResponse<Object> resp = new RestResponse<>();

        if (updatedProduct != null) {
            resp.setStatusCode(ApiStatusResponse.SUCCESS.getCode());
            resp.setMessage(ApiStatusResponse.SUCCESS.getMessage());
            resp.setData(updatedProduct);
            return ResponseEntity.ok(resp);
        } else {
            resp.setStatusCode(ApiStatusResponse.NOT_FOUND.getCode());
            resp.setMessage(ApiStatusResponse.NOT_FOUND.getMessage());
            resp.setData(null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> deleteProduct(@PathVariable int id) {
        boolean deleted = productService.deleteProduct(id);
        RestResponse<Object> resp = new RestResponse<>();

        if (deleted) {
            resp.setStatusCode(ApiStatusResponse.SUCCESS.getCode());
            resp.setMessage(ApiStatusResponse.SUCCESS.getMessage());
            resp.setData(null);
            return ResponseEntity.ok(resp);
        } else {
            resp.setStatusCode(ApiStatusResponse.NOT_FOUND.getCode());
            resp.setMessage(ApiStatusResponse.NOT_FOUND.getMessage());
            resp.setData(null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
        }
    }

    @GetMapping
    public ResponseEntity<PaginationResponse<ProductSearchResponse>> getAllProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size) {
        PaginationResponse<ProductSearchResponse> productPage = productService.getAllProduct(page, size);
        return ResponseEntity.ok(productPage);
    }
}
