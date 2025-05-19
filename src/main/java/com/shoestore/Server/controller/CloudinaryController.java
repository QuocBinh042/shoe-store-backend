package com.shoestore.Server.controller;

import com.shoestore.Server.dto.response.PaginationResponse;
import com.shoestore.Server.dto.response.SearchProductResponse;
import com.shoestore.Server.service.CloudinaryService;
import com.shoestore.Server.service.PaginationService;
import com.shoestore.Server.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cloudinary")
public class CloudinaryController {

    private final CloudinaryService cloudinaryService;
    private final PaginationService paginationService;
    private final ProductService productService;
    @GetMapping
    public ResponseEntity<PaginationResponse<Map<String, Object>>> listImages(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        try {
            Map result = cloudinaryService.listImagesInFolder("project_ShoeStore/ImageProduct/2");
            List<Map<String, Object>> resources = (List<Map<String, Object>>) result.get("resources");

            PaginationResponse<Map<String, Object>> pagination =
                    paginationService.paginate(resources, page, pageSize);

            return ResponseEntity.ok(pagination);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<Map> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "productId", required = false) String productId) {
        try {
            PaginationResponse<SearchProductResponse> tmp = productService.getAllProducts(1, 12);

            String folder = "project_ShoeStore/ImageProduct/"
                    + (productId != null && !productId.isEmpty() ? productId : tmp.getTotalElements() + 1);
            Map uploadResult = cloudinaryService.upload(file, folder);
            return ResponseEntity.ok(uploadResult);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }


}