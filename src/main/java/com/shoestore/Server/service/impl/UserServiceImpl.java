package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.request.SignUpRequest;
import com.shoestore.Server.dto.request.UserDTO;
import com.shoestore.Server.dto.response.PaginationResponse;
import com.shoestore.Server.dto.response.UserResponse;
import com.shoestore.Server.entities.Role;
import com.shoestore.Server.entities.User;
import com.shoestore.Server.enums.RoleType;
import com.shoestore.Server.enums.UserStatus;
import com.shoestore.Server.exception.UserAlreadyExistsException;
import com.shoestore.Server.mapper.UserMapper;
import com.shoestore.Server.repositories.OrderRepository;
import com.shoestore.Server.repositories.RoleRepository;
import com.shoestore.Server.repositories.UserRepository;
import com.shoestore.Server.service.PaginationService;
import com.shoestore.Server.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;
    private final PaginationService paginationService;
    private final UserMapper userMapper;

    @Override
    public UserResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new EntityNotFoundException("User not found with email: " + email);
        }
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse addUserByRegister(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists.");
        }
        Role customerRole = roleRepository.findByRoleType(RoleType.CUSTOMER)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot find role 'Customer'"));

        User user = userMapper.toSignUpRequest(signUpRequest);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setStatus(UserStatus.PENDING);
        user.setRoles(Set.of(customerRole));
        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse updateUserInformationByUser(int id, UserDTO updatedUserDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        existingUser.setName(updatedUserDTO.getName());
        existingUser.setPhoneNumber(updatedUserDTO.getPhoneNumber());
        existingUser.setEmail(updatedUserDTO.getEmail());
        existingUser = userRepository.save(existingUser);
        return userMapper.toResponse(existingUser);
    }

    @Override
    public UserResponse getUserById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return userMapper.toResponse(user);
    }

    @Override
    public void updateRefreshToken(String email, String refreshToken) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new EntityNotFoundException("User not found with email: " + email);
        }
        user.setRefreshToken(refreshToken);
        log.info("User updated successfully with email: {}", email);
        userRepository.save(user);
    }

    @Override
    public PaginationResponse<UserResponse> getAllUsers(int page, int size) {
        Pageable pageable = paginationService.createPageable(page, size);
        Page<User> usersPage = userRepository.findAll(pageable);
        Page<UserResponse> userResponsesPage = usersPage.map(userMapper::toResponse);
        return paginationService.paginate(userResponsesPage);
    }

    @Override
    public PaginationResponse<UserResponse> getUsersByRoleCustomer(int page, int size) {
        Pageable pageable = paginationService.createPageable(page, size);
        Page<User> usersPage = userRepository.findByRoles_RoleType(RoleType.CUSTOMER, pageable);
        Page<UserResponse> userResponsesPage = usersPage.map(userMapper::toResponse);
        return paginationService.paginate(userResponsesPage);
    }

    @Override
    public PaginationResponse<UserResponse> searchUsers(String keyword, int page, int size) {
        Pageable pageable = paginationService.createPageable(page, size);
        Page<User> usersPage = userRepository.searchUsersByRole(keyword, RoleType.CUSTOMER, pageable);
        Page<UserResponse> userResponsesPage = usersPage.map(userMapper::toResponse);
        return paginationService.paginate(userResponsesPage);
    }

    @Override
    public UserResponse createCustomer(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists.");
        }
        Role customerRole = roleRepository.findByRoleType(RoleType.CUSTOMER)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Role 'CUSTOMER' not found"));

        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setStatus(UserStatus.PENDING);
        user.setRoles(Set.of(customerRole));
        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }


    @Override
    public int countDeliveredOrdersByUserId(int userId) {
        return orderRepository.countDeliveredOrdersByUserId(userId);
    }

    @Override
    public Double calculateTotalAmountByUserId(int userId) {
        Double total = orderRepository.sumTotalAmountByUserId(userId);
        return total != null ? total : 0.0;
    }
    @Override
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void changePassword(int userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }


    @Override
    public UserResponse updateUserStatus(int id, String status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        try {
            user.setStatus(UserStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value: " + status);
        }
        userRepository.save(user);
        return userMapper.toResponse(user);
    }

}