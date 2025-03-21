package com.shoestore.Server.service;
import com.shoestore.Server.dto.request.SupplierDTO;
import com.shoestore.Server.entities.Supplier;

import java.util.List;

public interface SupplierService {
    SupplierDTO getSupplierById(int id);
    List<SupplierDTO> getAllSuppliers();
}
