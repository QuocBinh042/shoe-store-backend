package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.ProductDTO;
import com.shoestore.Server.dto.request.ProductDetailDTO;
import com.shoestore.Server.dto.response.ApiStatusResponse;
import com.shoestore.Server.dto.response.ProductDetailResponse;
import com.shoestore.Server.dto.response.RestResponse;
import com.shoestore.Server.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product-details")
public class ProductDetailController {
    private final ProductDetailService productDetailService;
    private final ProductService productService;
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final PromotionService promotionService;


    @GetMapping("/by-product-id/{id}")
    public ResponseEntity<ProductDetailResponse> getProductDetailsByProductId(@PathVariable int id) {
        ProductDTO productDTO = productService.getProductById(id);
        if (productDTO == null) {
            return ResponseEntity.notFound().build();
        }

        List<ProductDetailDTO> productDetails = productDetailService.getByProductId(id);
        ProductDetailResponse response = new ProductDetailResponse(
                productDetails,
                productDTO.getProductName(),
                categoryService.getCategory(productDTO.getCategoryID()).getName(),
                brandService.getBrandById(productDTO.getBrandID()).getName(),
                productDTO.getDescription(),
                productDTO.getPrice(),
                productDTO.getImageURL(),
                promotionService.getDiscountedPrice(productDTO.getProductID())
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-order-detail-id/{id}")
    public ResponseEntity<List<ProductDetailDTO>> getProductDetailsByOrderDetailId(@PathVariable int id) {
        return ResponseEntity.ok(productDetailService.getByProductId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailDTO> getProductDetailsById(@PathVariable int id) {
        ProductDetailDTO productDetail = productDetailService.getProductDetailById(id);
        return productDetail != null ? ResponseEntity.ok(productDetail) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{productId}")
    public ResponseEntity<RestResponse<Object>> createProductDetail(
            @PathVariable int productId,
            @Valid @RequestBody ProductDetailDTO productDetailDTO) {

        ProductDetailDTO createdDetail = productDetailService.createProductDetail(productId, productDetailDTO);
        RestResponse<Object> resp = new RestResponse<>();

        if (createdDetail != null) {
            resp.setStatusCode(ApiStatusResponse.CREATED.getCode());
            resp.setMessage(ApiStatusResponse.CREATED.getMessage());
            resp.setData(createdDetail);
            return ResponseEntity.status(ApiStatusResponse.CREATED.getCode()).body(resp);
        } else {
            resp.setStatusCode(ApiStatusResponse.NOT_FOUND.getCode());
            resp.setMessage(ApiStatusResponse.NOT_FOUND.getMessage());
            resp.setData(null);
            return ResponseEntity.status(ApiStatusResponse.NOT_FOUND.getCode()).body(resp);
        }
    }
    @PutMapping("/{detailId}")
    public ResponseEntity<RestResponse<Object>> updateProductDetail(
            @PathVariable int detailId,
            @Valid @RequestBody ProductDetailDTO productDetailDTO) {

        ProductDetailDTO updatedDetail = productDetailService.updateProductDetail(detailId, productDetailDTO);
        RestResponse<Object> resp = new RestResponse<>();

        if (updatedDetail != null) {
            resp.setStatusCode(ApiStatusResponse.SUCCESS.getCode());
            resp.setMessage(ApiStatusResponse.SUCCESS.getMessage());
            resp.setData(updatedDetail);
            return ResponseEntity.ok(resp);
        } else {
            resp.setStatusCode(ApiStatusResponse.NOT_FOUND.getCode());
            resp.setMessage(ApiStatusResponse.NOT_FOUND.getMessage());
            resp.setData(null);
            return ResponseEntity.status(ApiStatusResponse.NOT_FOUND.getCode()).body(resp);
        }
    }



}
