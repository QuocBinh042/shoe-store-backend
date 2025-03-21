package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.request.SupplierDTO;
import com.shoestore.Server.entities.Supplier;
import com.shoestore.Server.mapper.SupplierMapper;
import com.shoestore.Server.repositories.SupplierRepository;
import com.shoestore.Server.service.SupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    public SupplierServiceImpl(SupplierRepository supplierRepository, SupplierMapper supplierMapper) {
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
    }

    @Override
    public SupplierDTO getSupplierById(int id) {
        log.info("Fetching supplier with ID: {}", id);
        Supplier supplier = supplierRepository.findBySupplierID(id);
        if (supplier == null) {
            log.warn("Supplier not found with ID: {}", id);
            return null;
        }
        return supplierMapper.toDto(supplier);
    }

    @Override
    public List<SupplierDTO> getAllSuppliers() {
        log.info("Fetching all suppliers from database.");
        List<SupplierDTO> suppliers = supplierRepository.findAll().stream()
                .map(supplierMapper::toDto)
                .collect(Collectors.toList());
        log.info("Found {} suppliers.", suppliers.size());
        return suppliers;
    }
}
