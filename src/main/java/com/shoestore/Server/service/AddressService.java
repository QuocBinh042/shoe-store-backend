package com.shoestore.Server.service;

import com.shoestore.Server.dto.request.AddressDTO;
import com.shoestore.Server.entities.Address;

import java.util.List;

public interface AddressService {
    List<AddressDTO> getAddressByUserId(int id);
    AddressDTO getById(int id);
    void deleteById(int id);
    AddressDTO updateAddress(int id,AddressDTO addressUpdate);
    AddressDTO addAddress(AddressDTO addressDTOss);
}
