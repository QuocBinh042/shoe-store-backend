package com.shoestore.Server.service;

import com.shoestore.Server.dto.request.SignUpRequest;
import com.shoestore.Server.dto.request.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO findByEmail(String email);

    UserDTO addUserByRegister(SignUpRequest signUpRequest);

    List<UserDTO> getAllUsers();

    UserDTO updateUserInformationByUser(int id, UserDTO updatedUserDTO);

    UserDTO getUserById(int id);
    void updateRefreshToken(String email, String refreshToken);
    List<UserDTO> getUsersByRoleCustomer();

    UserDTO createCustomer(UserDTO userDTO);
    List<UserDTO> searchUsers(String keyword);
    int countDeliveredOrdersByUserId(int userId);
    Double calculateTotalAmountByUserId(int userId);
}
