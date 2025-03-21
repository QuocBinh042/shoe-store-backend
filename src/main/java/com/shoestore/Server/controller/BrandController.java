package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.BrandDTO;
import com.shoestore.Server.dto.response.ApiStatusResponse;
import com.shoestore.Server.dto.response.RestResponse;
import com.shoestore.Server.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;
    @GetMapping
    public ResponseEntity<RestResponse<Object>> getAllBrands() {
        List<BrandDTO> brands = brandService.getAllBrands();
        RestResponse<Object> resp = new RestResponse<>();
        resp.setStatusCode(ApiStatusResponse.SUCCESS.getCode());
        resp.setMessage(ApiStatusResponse.SUCCESS.getMessage());
        resp.setData(brands);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("{id}")
    public ResponseEntity<RestResponse<Object>> getBrandById(@PathVariable int id) {
       BrandDTO brand = brandService.getBrandById(id);
        RestResponse<Object> resp = new RestResponse<>();

        if (brand != null) {
            resp.setStatusCode(ApiStatusResponse.SUCCESS.getCode());
            resp.setMessage(ApiStatusResponse.SUCCESS.getMessage());
            resp.setData(brand);
            return ResponseEntity.ok(resp);
        } else {
            resp.setStatusCode(ApiStatusResponse.NOT_FOUND.getCode());
            resp.setMessage(ApiStatusResponse.NOT_FOUND.getMessage());
            resp.setData(null);
            return ResponseEntity.status(ApiStatusResponse.NOT_FOUND.getCode()).body(resp);
        }
    }
}
