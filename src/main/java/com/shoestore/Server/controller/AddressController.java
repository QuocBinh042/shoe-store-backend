package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.AddressDTO;
import com.shoestore.Server.service.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN','SUPER_ADMIN')")
    @GetMapping("/by-user-id/{id}")
    public ResponseEntity<List<AddressDTO>> getAddressByUserId(@PathVariable int id) {
        return ResponseEntity.ok(addressService.getAddressByUserId(id));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable int id) {
        AddressDTO address = addressService.getById(id);
        return address != null ? ResponseEntity.ok(address) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable int id) {
        addressService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable int id, @RequestBody AddressDTO addressDTO) {
        AddressDTO updatedAddress = addressService.updateAddress(id, addressDTO);
        return updatedAddress != null ? ResponseEntity.ok(updatedAddress) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/add")
    public ResponseEntity<AddressDTO> addAddress(@RequestBody AddressDTO addressDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.addAddress(addressDTO));
    }
}
