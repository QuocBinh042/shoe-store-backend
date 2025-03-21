package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.SupplierDTO;
import com.shoestore.Server.dto.response.ApiStatusResponse;
import com.shoestore.Server.dto.response.RestResponse;
import com.shoestore.Server.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;

    @GetMapping
    public ResponseEntity<RestResponse<Object>> getAllSuppliers() {
        List<SupplierDTO> suppliers = supplierService.getAllSuppliers();
        RestResponse<Object> resp = new RestResponse<>();
        resp.setStatusCode(ApiStatusResponse.SUCCESS.getCode());
        resp.setMessage(ApiStatusResponse.SUCCESS.getMessage());
        resp.setData(suppliers);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("{id}")
    public ResponseEntity<RestResponse<Object>> getSupplierById(@PathVariable int id) {
        SupplierDTO supplier = supplierService.getSupplierById(id);
        RestResponse<Object> resp = new RestResponse<>();

        if (supplier != null) {
            resp.setStatusCode(ApiStatusResponse.SUCCESS.getCode());
            resp.setMessage(ApiStatusResponse.SUCCESS.getMessage());
            resp.setData(supplier);
            return ResponseEntity.ok(resp);
        } else {
            resp.setStatusCode(ApiStatusResponse.NOT_FOUND.getCode());
            resp.setMessage(ApiStatusResponse.NOT_FOUND.getMessage());
            resp.setData(null);
            return ResponseEntity.status(ApiStatusResponse.NOT_FOUND.getCode()).body(resp);
        }
    }
}
