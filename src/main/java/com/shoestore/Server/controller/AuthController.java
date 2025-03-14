package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.LoginRequest;
import com.shoestore.Server.dto.request.UserDTO;
import com.shoestore.Server.dto.response.LoginResponse;
import com.shoestore.Server.dto.response.RestResponse;
import com.shoestore.Server.dto.response.UserInsideTokenResponse;
import com.shoestore.Server.dto.response.UserLoginResponse;
import com.shoestore.Server.exception.UserNotActiveException;
import com.shoestore.Server.security.CustomUserDetailsService;
import com.shoestore.Server.service.UserService;
import com.shoestore.Server.security.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
public class AuthController {

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;
    @Value("${jwt.expiration}")
    private long accessExpirationMs;

    @Value("${jwt.refreshExpiration}")
    private long refreshExpirationMs;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getEmail());
            if (!passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new RestResponse<>(HttpStatus.UNAUTHORIZED.value(), "Password is invalid", null, null));
            }
            UserDTO userDB = userService.findByEmail(loginRequest.getEmail());
            LoginResponse loginResponse = new LoginResponse();
            if (userDB != null && userDB.getStatus().equals("Active")) {
                UserLoginResponse user = new UserLoginResponse(
                        userDB.getUserID(),
                        userDB.getEmail(),
                        userDB.getPhoneNumber(),
                        userDB.getName(),
                        userDB.getRole()
                );
                loginResponse.setUser(user);
            } else {
                throw new UserNotActiveException("User is not activated");
            }
            String accessToken = jwtUtil.createAccessToken(loginResponse.getUser().getEmail(), loginResponse);
            loginResponse.setAccessToken(accessToken);
            Instant refreshExpirationTime = Instant.now().plus(refreshExpirationMs, ChronoUnit.MILLIS);

            String refreshToken = jwtUtil.createRefreshToken(loginResponse.getUser().getEmail(), loginResponse, refreshExpirationTime);

            userService.updateRefreshToken(loginRequest.getEmail(), refreshToken);

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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RestResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred", e.getMessage(), null));
        }
    }


    @PostMapping("/sign-up")
    public ResponseEntity<?> register(@RequestBody UserDTO user) {
        try {
            UserDTO newUser = userService.addUserByRegister(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Collections.singletonMap("message", "User registered successfully."));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Collections.singletonMap("message", e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "An unexpected error occurred. Please try again."));
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
    public ResponseEntity<?> refreshToken(@CookieValue(value = "refresh_token",  required = false) String refreshToken) {
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
        UserDTO userDB = userService.findByEmail(email);
        if (userDB == null || !userDB.getStatus().equals("Active") || !refreshToken.equals(userDB.getRefreshToken())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new RestResponse<>(HttpStatus.UNAUTHORIZED.value(), "Invalid refresh token.", null, null));
        }
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUser(new UserLoginResponse(
                userDB.getUserID(),
                userDB.getEmail(),
                userDB.getPhoneNumber(),
                userDB.getName(),
                userDB.getRole()
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
        UserDTO userDTO=userService.getUserById(userId);

        return ResponseEntity.ok().body(userDTO);
    }




}
