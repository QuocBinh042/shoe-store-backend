package com.shoestore.Server.service;

import com.shoestore.Server.dto.request.UserDTO;
import com.shoestore.Server.entities.User;

import java.util.List;

public interface UserService {
    UserDTO findByEmail(String email);

    UserDTO addUserByRegister(UserDTO userDTO);

    List<UserDTO> getAllUsers();

    UserDTO updateUserInformationByUser(int id, UserDTO updatedUserDTO);

    UserDTO getUserById(int id);
    void updateRefreshToken(String email, String refreshToken);
}
