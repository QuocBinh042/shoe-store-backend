package com.shoestore.Server.service;

import com.shoestore.Server.dto.request.SignUpRequest;
import com.shoestore.Server.dto.request.UserDTO;
import com.shoestore.Server.dto.response.PaginationResponse;
import com.shoestore.Server.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserResponse findByEmail(String email);

    UserResponse addUserByRegister(SignUpRequest signUpRequest);

    UserResponse updateUserInformationByUser(int id, UserDTO updatedUserDTO);

    UserResponse getUserById(int id);

    void updateRefreshToken(String email, String refreshToken);

    PaginationResponse<UserResponse> getAllUsers(int page, int size);

    PaginationResponse<UserResponse> getUsersByRoleCustomer(int page, int size);

    PaginationResponse<UserResponse> searchUsers(String keyword, int page, int size);

    UserResponse createCustomer(UserDTO userDTO);

    UserResponse updateUserStatus(int id, String status);

    int countDeliveredOrdersByUserId(int userId);

    Double calculateTotalAmountByUserId(int userId);
    boolean isEmailExists(String email);
    void changePassword(int userId, String currentPassword, String newPassword);
    void forgotPassword(String email,String newPassword);

    UserResponse updateCustomerGroupByTotalAmount(int userId);
}
