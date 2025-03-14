package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.request.AddressDTO;
import com.shoestore.Server.entities.Address;
import com.shoestore.Server.entities.User;
import com.shoestore.Server.mapper.AddressMapper;
import com.shoestore.Server.repositories.AddressRepository;
import com.shoestore.Server.repositories.UserRepository;
import com.shoestore.Server.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
@Service
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final UserRepository userRepository;
    public AddressServiceImpl(AddressRepository addressRepository, AddressMapper addressMapper, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
        this.userRepository = userRepository;
    }

    @Override
    public List<AddressDTO> getAddressByUserId(int id) {
        List<Address> addresses = addressRepository.findAddressByUserId(id);
        return addresses.stream()
                .map(addressMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AddressDTO getById(int id) {
        log.info("Fetching address with ID: {}", id);
        Optional<Address> addressOpt = addressRepository.findById(id);
        if (addressOpt.isPresent()) {
            log.info("Found address with ID: {}", id);
            return addressMapper.toDto(addressOpt.get());
        } else {
            log.warn("Address with ID {} not found", id);
            return null;
        }
    }

    @Override
    public void deleteById(int id) {
        addressRepository.deleteById(id);
        log.info("Deleted address with ID: {}", id);
    }

    @Override
    public AddressDTO updateAddress(int id, AddressDTO addressDTO) {
        Optional<Address> existAddress = addressRepository.findById(id);
        if (existAddress.isPresent()) {
            Address addressToUpdate = existAddress.get();
            addressToUpdate.setCity(addressDTO.getCity());
            addressToUpdate.setPhone(addressDTO.getPhone());
            addressToUpdate.setDistrict(addressDTO.getDistrict());
            addressToUpdate.setStreet(addressDTO.getStreet());
            addressToUpdate.setFullName(addressDTO.getFullName());
            addressToUpdate.setDefault(addressDTO.isDefault());
            addressToUpdate.setWard(addressDTO.getWard());
            addressToUpdate.setType(addressDTO.getType());
            Address updatedAddress = addressRepository.save(addressToUpdate);
            return addressMapper.toDto(updatedAddress);
        }
        return null;
    }

    @Override
    public AddressDTO addAddress(AddressDTO addressDTO) {
        log.info("Adding new address for user ID: {}", addressDTO.getUser().getUserID());
        User user = userRepository.findById(addressDTO.getUser().getUserID())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Address address = addressMapper.toEntity(addressDTO);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        log.info("Added new address with ID: {}", savedAddress.getAddressID());
        return addressMapper.toDto(savedAddress);
    }
}
