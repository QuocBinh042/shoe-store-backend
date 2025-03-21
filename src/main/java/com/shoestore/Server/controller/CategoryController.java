package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.CategoryDTO;
import com.shoestore.Server.dto.response.ApiStatusResponse;
import com.shoestore.Server.dto.response.RestResponse;
import com.shoestore.Server.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<RestResponse<Object>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        RestResponse<Object> resp = new RestResponse<>();
        resp.setStatusCode(ApiStatusResponse.SUCCESS.getCode());
        resp.setMessage(ApiStatusResponse.SUCCESS.getMessage());
        resp.setData(categories);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("{id}")
    public ResponseEntity<RestResponse<Object>> getCategoryById(@PathVariable int id) {
        CategoryDTO category = categoryService.getCategory(id);
        RestResponse<Object> resp = new RestResponse<>();

        if (category != null) {
            resp.setStatusCode(ApiStatusResponse.SUCCESS.getCode());
            resp.setMessage(ApiStatusResponse.SUCCESS.getMessage());
            resp.setData(category);
            return ResponseEntity.ok(resp);
        } else {
            resp.setStatusCode(ApiStatusResponse.NOT_FOUND.getCode());
            resp.setMessage(ApiStatusResponse.NOT_FOUND.getMessage());
            resp.setData(null);
            return ResponseEntity.status(ApiStatusResponse.NOT_FOUND.getCode()).body(resp);
        }
    }

}
