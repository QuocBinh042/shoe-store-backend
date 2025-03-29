package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.request.SignUpRequest;
import com.shoestore.Server.dto.request.UserDTO;
import com.shoestore.Server.entities.Role;
import com.shoestore.Server.entities.User;
import com.shoestore.Server.enums.RoleType;
import com.shoestore.Server.mapper.UserMapper;
import com.shoestore.Server.repositories.OrderRepository;
import com.shoestore.Server.repositories.RoleRepository;
import com.shoestore.Server.repositories.UserRepository;
import com.shoestore.Server.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return null;
        }
        return userMapper.toDto(user);
    }
    @Override
    public UserDTO addUserByRegister(SignUpRequest signUpRequest) {
        System.out.println(signUpRequest);
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists.");
        }
        Role customerRole = roleRepository.findByRoleType(RoleType.CUSTOMER)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot find role 'Customer'"));

        User user = userMapper.toSignUpRequest(signUpRequest);
        System.out.println(user);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setStatus("Active");
        user.setRoles(Set.of(customerRole));
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }


    @Override
    public UserDTO updateUserInformationByUser(int id, UserDTO updatedUserDTO) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) {
            return null;
        }
        existingUser.setName(updatedUserDTO.getName());
        existingUser.setPhoneNumber(updatedUserDTO.getPhoneNumber());
        existingUser.setEmail(updatedUserDTO.getEmail());
        existingUser.setCI(updatedUserDTO.getCI());
        existingUser = userRepository.save(existingUser);
        return userMapper.toDto(existingUser);
    }

    @Override
    public UserDTO getUserById(int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }
        return userMapper.toDto(user);
    }

    @Override
    public void updateRefreshToken(String email, String refreshToken) {
        User user=userRepository.findByEmail(email);
        System.out.println(user.getUserID());
        if(user == null){
            log.info("User not found");
        }
        user.setRefreshToken(refreshToken);
        log.info("User updated successfully");
        userRepository.save(user);


    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users= userRepository.findAll();
        return users.stream().map(userMapper::toDto).collect(Collectors.toList());
    }


    @Override
    public List<UserDTO> getUsersByRoleCustomer() {
        List<User> users = userRepository.findByRoles_RoleType(RoleType.CUSTOMER);
        return users.stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public UserDTO createCustomer(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists.");
        }
        Role customerRole = roleRepository.findByRoleType(RoleType.CUSTOMER)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Role 'CUSTOMER' not found'"));

        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setStatus("Active");
        user.setRoles(Set.of(customerRole));
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public List<UserDTO> searchUsers(String keyword) {
        List<User> users = userRepository.searchUsersByRole(keyword, RoleType.CUSTOMER);
        return users.stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public int countDeliveredOrdersByUserId(int userId) {
        return orderRepository.countDeliveredOrdersByUserId(userId);
    }

    @Override
    public Double calculateTotalAmountByUserId(int userId) {
        return orderRepository.sumTotalAmountByUserId(userId);
    }

    @Override
    public UserDTO updateUserStatus(int id, String status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        user.setStatus(status);
        userRepository.save(user);
        return userMapper.toDto(user);
    }
}
