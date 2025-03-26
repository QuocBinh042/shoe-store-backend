package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.UserDTO;
import com.shoestore.Server.dto.response.ApiStatusResponse;
import com.shoestore.Server.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/by-user-id/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id) {
        UserDTO userDTO = userService.getUserById(id);
        return (userDTO != null) ? ResponseEntity.ok(userDTO) : ResponseEntity.notFound().build();
    }

    @GetMapping("/by-email")
    public ResponseEntity<UserDTO> findByEmail(@RequestParam String email) {
        UserDTO userDTO = userService.findByEmail(email);
        return (userDTO != null) ? ResponseEntity.ok(userDTO) : ResponseEntity.notFound().build();
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<UserDTO>> findAllUsers() {
        List<UserDTO> userDTOs = userService.getAllUsers();
        return ResponseEntity.ok(userDTOs);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable int id,@Valid @RequestBody UserDTO userDTO) {
        UserDTO existingUser = userService.getUserById(id);
        if (existingUser == null) return ResponseEntity.notFound().build();

        UserDTO updatedUser = userService.updateUserInformationByUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/customers")
    public ResponseEntity<List<UserDTO>> getCustomers() {
        List<UserDTO> customers = userService.getUsersByRoleCustomer();
        return ResponseEntity.ok(customers);
    }

    @PostMapping("/customers")
    public ResponseEntity<UserDTO> createCustomer(@Valid @RequestBody UserDTO userDTO) {
        UserDTO newCustomer = userService.createCustomer(userDTO);
        return ResponseEntity.status(ApiStatusResponse.CREATED.getCode()).body(newCustomer);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String keyword) {
        List<UserDTO> users = userService.searchUsers(keyword);
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
}
