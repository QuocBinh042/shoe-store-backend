package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.ChangePasswordRequest;
import com.shoestore.Server.dto.request.LoginRequest;
import com.shoestore.Server.dto.request.SignUpRequest;
import com.shoestore.Server.dto.request.UserDTO;
import com.shoestore.Server.dto.response.*;
import com.shoestore.Server.entities.User;
import com.shoestore.Server.enums.UserStatus;
import com.shoestore.Server.exception.UserNotActiveException;
import com.shoestore.Server.security.CustomUserDetailsService;
import com.shoestore.Server.service.EmailService;
import com.shoestore.Server.service.UserService;
import com.shoestore.Server.security.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    private final UserService userService;
    private final EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private JwtUtils jwtUtils;
    @Value("${jwt.refreshExpiration}")
    private long refreshExpirationMs;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        System.out.println("Nhân"+loginRequest);
        try {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getEmail());
            if (!passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new RestResponse<>(HttpStatus.UNAUTHORIZED.value(), "Password is invalid", null, null));
            }

            UserResponse userDB = userService.findByEmail(loginRequest.getEmail());

            if (userDB == null || !userDB.getStatus().equals(UserStatus.ACTIVE)) {
                throw new UserNotActiveException("User is not activated");
            }
            UserLoginResponse user = new UserLoginResponse(
                    userDB.getUserID(),
                    userDB.getEmail(),
                    userDB.getPhoneNumber(),
                    userDB.getName(),
                    userDB.getRoles()
            );

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setUser(user);

            String accessToken = jwtUtil.createAccessToken(user.getEmail(), loginResponse);
            loginResponse.setAccessToken(accessToken);

            Instant refreshExpirationTime = Instant.now().plus(refreshExpirationMs, ChronoUnit.MILLIS);
            String refreshToken = jwtUtil.createRefreshToken(user.getEmail(), loginResponse, refreshExpirationTime);
            userService.updateRefreshToken(user.getEmail(), refreshToken);

            ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                    .httpOnly(true).path("/")
                    .secure(true)
                    .maxAge(refreshExpirationMs / 1000)
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(loginResponse);

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new RestResponse<>(HttpStatus.NOT_FOUND.value(), "Email does not exist", null, null));
        }

    }
    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmailExists(@RequestParam String email) {
        boolean exists = userService.isEmailExists(email);
        return ResponseEntity.ok(exists);
    }
    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendOTP(@RequestParam String email) {
        UserResponse user =userService.findByEmail(email);
        if (UserStatus.ACTIVE.equals(user.getStatus())) {
            return ResponseEntity.ok(new RestResponse<>(ApiStatusResponse.BAD_REQUEST.getCode(), "Verification code resent.", null,null));
        }

        emailService.sendVerificationCodeEmail(email, user.getName());
        return ResponseEntity.ok(new RestResponse<>(ApiStatusResponse.SUCCESS.getCode(), "Verification code resent.", null,null));
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOTP(@RequestParam String email, @RequestParam String otp) {
        String storedOtp = redisTemplate.opsForValue().get("OTP:" + email);
        if (storedOtp == null) {
            return ResponseEntity.ok(new RestResponse<>(ApiStatusResponse.BAD_REQUEST.getCode(), "OTP has expired or does not exist.", null,null));
        }
        else if (!storedOtp.equals(otp)) {
            return ResponseEntity.ok(new RestResponse<>(ApiStatusResponse.BAD_REQUEST.getCode(), "Invalid OTP.", null,null));
        }
        else{
            redisTemplate.delete("OTP:" + email);
            UserResponse user = userService.findByEmail(email);
            if (user != null) {
                userService.updateUserStatus(user.getUserID(),"ACTIVE");
            }
        }
        return ResponseEntity.ok(new RestResponse<>(ApiStatusResponse.SUCCESS.getCode(), "Email verified successfully.", null,null));
    }
    @PostMapping("/sign-up")
    public ResponseEntity<RestResponse<Object>> register(@Valid @RequestBody SignUpRequest signUpRequest) {
        try {
            UserResponse newUser = userService.addUserByRegister(signUpRequest);
            return ResponseEntity.ok(new RestResponse<>(ApiStatusResponse.SUCCESS.getCode(), "User registered successfully", null, newUser));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(new RestResponse<>(e.getStatusCode().value(), e.getReason(), null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RestResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", e.getMessage(), null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(value = "refresh_token", required = false) String refreshToken, HttpServletResponse response) {
        if (refreshToken != null) {
            Optional<Claims> claimsOpt = jwtUtil.checkValidJWTRefreshToken(refreshToken);
            if (claimsOpt.isPresent()) {
                String email = claimsOpt.get().getSubject();
                userService.updateRefreshToken(email, null);
            }
        }
        ResponseCookie clearedCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true).secure(true).path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, clearedCookie.toString())
                .body(Collections.singletonMap("result", "Logged out successfully"));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@CookieValue(value = "refresh_token", required = false) String refreshToken) throws UserNotActiveException {
        System.out.println("Đã xin access token");
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new RestResponse<>(HttpStatus.UNAUTHORIZED.value(), "Missing refresh token.", null, null));
        }
        Optional<Claims> claimsOpt = jwtUtil.checkValidJWTRefreshToken(refreshToken);
        if (claimsOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new RestResponse<>(HttpStatus.UNAUTHORIZED.value(), "Invalid or expired refresh token.", null, null));
        }

        Claims claims = claimsOpt.get();
        String email = claims.getSubject();
        UserResponse userDB = userService.findByEmail(email);
//        if (!userDB.getStatus().equalsIgnoreCase("Active")) {
//            throw new UserNotActiveException("User is not activated.");
//        }

        if (!refreshToken.equals(userDB.getRefreshToken())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new RestResponse<>(HttpStatus.UNAUTHORIZED.value(), "Invalid refresh token.", null, null));
        }
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUser(new UserLoginResponse(
                userDB.getUserID(),
                userDB.getEmail(),
                userDB.getPhoneNumber(),
                userDB.getName(),
                userDB.getRoles()
        ));

        String accessToken = jwtUtil.createAccessToken(email, loginResponse);
        loginResponse.setAccessToken(accessToken);

        long refreshExpTimestamp = claims.get("refresh_exp", Long.class);
        Instant originalExpiration = Instant.ofEpochSecond(refreshExpTimestamp);

        String newRefreshToken = jwtUtil.createRefreshToken(email, loginResponse, originalExpiration);
        userService.updateRefreshToken(email, newRefreshToken);

        ResponseCookie cookie = ResponseCookie.from("refresh_token", newRefreshToken)
                .httpOnly(true).path("/")
                .secure(true)
                .maxAge(refreshExpTimestamp - Instant.now().getEpochSecond())
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(loginResponse);
    }
    private int getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Integer) {
            return (Integer) principal;
        }
        throw new BadCredentialsException("User ID not found in JWT or invalid type");
    }
    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String token = authHeader.substring(7);
        Optional<Claims> claimsOpt = jwtUtils.parseClaims(token);

        if (claimsOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new RestResponse<>(HttpStatus.UNAUTHORIZED.value(), "Missing refresh token.", null, null));
        }

        Claims claims = claimsOpt.get();
        Object userObj = claims.get("user");
        Map<String, Object> userMap = (Map<String, Object>) userObj;
        int userId = (Integer) userMap.get("id");
        UserResponse userDTO = userService.getUserById(userId);

        return ResponseEntity.ok().body(userDTO);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        int userId = getCurrentUserId();
        try {
            userService.changePassword(userId, request.getCurrentPassword(), request.getNewPassword());
            return ResponseEntity.ok(new RestResponse<>(HttpStatus.OK.value(), "Password changed successfully", null, null));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new RestResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null, null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RestResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RestResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred", e.getMessage(), null));
        }
    }



}
