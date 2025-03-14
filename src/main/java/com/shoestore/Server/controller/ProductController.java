package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.ProductDTO;
import com.shoestore.Server.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
