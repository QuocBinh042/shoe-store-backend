package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.ProductDTO;
import com.shoestore.Server.dto.request.ProductDetailDTO;
import com.shoestore.Server.dto.request.ProductDetailRequest;
import com.shoestore.Server.dto.response.ApiStatusResponse;
import com.shoestore.Server.dto.response.OverviewProductResponse;
import com.shoestore.Server.dto.response.ProductDetailsResponse;
import com.shoestore.Server.dto.response.RestResponse;
import com.shoestore.Server.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product-details")
public class ProductDetailController {
    private final ProductDetailService productDetailService;
    private final ProductService productService;

    @GetMapping("/by-product-id/{id}")
    public ResponseEntity<OverviewProductResponse> getProductDetailsByProductId(@PathVariable int id) {
        OverviewProductResponse response = productDetailService.getProductOverviewById(id);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-order-detail-id/{id}")
    public ResponseEntity<List<ProductDetailsResponse>> getProductDetailsByOrderDetailId(@PathVariable int id) {
        return ResponseEntity.ok(productDetailService.getByProductId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailsResponse> getProductDetailsById(@PathVariable int id) {
        ProductDetailsResponse productDetail = productDetailService.getProductDetailById(id);
        return productDetail != null ? ResponseEntity.ok(productDetail) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{productId}")
    public ResponseEntity<RestResponse<Object>> createProductDetail(
            @PathVariable int productId,
            @Valid @RequestBody ProductDetailRequest productDetailRequest) {

        ProductDetailsResponse createdDetail = productDetailService.createProductDetail(productId, productDetailRequest);
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
            @Valid @RequestBody ProductDetailRequest productDetailRequest) {

        ProductDetailsResponse updatedDetail = productDetailService.updateProductDetail(detailId, productDetailRequest);
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
