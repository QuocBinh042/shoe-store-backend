package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.ProductDTO;
import com.shoestore.Server.dto.response.RestResponse;
import com.shoestore.Server.entities.Product;
import com.shoestore.Server.repositories.ProductRepository;
import com.shoestore.Server.service.ProductService;
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

}
