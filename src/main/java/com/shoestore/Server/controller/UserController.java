package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.UserDTO;
import com.shoestore.Server.service.UserService;
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
    public ResponseEntity<UserDTO> updateUser(@PathVariable int id, @RequestBody UserDTO userDTO) {
        UserDTO existingUser = userService.getUserById(id);
        if (existingUser == null) return ResponseEntity.notFound().build();

        UserDTO updatedUser = userService.updateUserInformationByUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }
}
