package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.UserDTO;
import com.shoestore.Server.dto.response.PaginationResponse;
import com.shoestore.Server.dto.response.UserResponse;
import com.shoestore.Server.service.UserService;
import com.shoestore.Server.utils.AppConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable int id) {
        UserResponse userResponse = userService.getUserById(id);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/by-email")
    public ResponseEntity<UserResponse> findByEmail(@RequestParam String email) {
        UserResponse userResponse = userService.findByEmail(email);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable int id, @Valid @RequestBody UserDTO userDTO) {
        UserResponse updatedUser = userService.updateUserInformationByUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }


    @PostMapping("/customers")
    public ResponseEntity<UserResponse> createCustomer(@Valid @RequestBody UserDTO userDTO) {
        UserResponse newCustomer = userService.createCustomer(userDTO);
        return ResponseEntity.status(201).body(newCustomer);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<UserResponse> updateUserStatus(@PathVariable int id,
                                                         @RequestBody Map<String, String> requestBody) {
        String newStatus = requestBody.get("status");
        if (newStatus == null || newStatus.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status cannot be empty");
        }
        UserResponse updatedUser = userService.updateUserStatus(id, newStatus);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/customers")
    public ResponseEntity<PaginationResponse<UserResponse>> getCustomers(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) int page,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE) int size) {
        PaginationResponse<UserResponse> customers = userService.getUsersByRoleCustomer(page, size);
        return ResponseEntity.ok(customers);
    }

    @GetMapping
    public ResponseEntity<PaginationResponse<UserResponse>> findAllUsers(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) int page,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE) int size) {
        PaginationResponse<UserResponse> userResponses = userService.getAllUsers(page, size);
        return ResponseEntity.ok(userResponses);
    }

    @GetMapping("/search")
    public ResponseEntity<PaginationResponse<UserResponse>> searchUsers(
            @RequestParam String keyword,
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) int page,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE) int size) {
        PaginationResponse<UserResponse> users = userService.searchUsers(keyword, page, size);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}/delivered-orders-count")
    public ResponseEntity<Integer> getDeliveredOrdersCount(@PathVariable int id) {
        int count = userService.countDeliveredOrdersByUserId(id);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{id}/total-amount")
    public ResponseEntity<Double> getTotalAmountByUserId(@PathVariable int id) {
        Double totalAmount = userService.calculateTotalAmountByUserId(id);
        return ResponseEntity.ok(totalAmount);
    }

    @PatchMapping("/{id}/update-customer-group")
    public ResponseEntity<UserResponse> updateCustomerGroup(@PathVariable int id) {
        UserResponse updatedUser = userService.updateCustomerGroupByTotalAmount(id);
        return ResponseEntity.ok(updatedUser);
    }

}