package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.CartItemDTO;
import com.shoestore.Server.dto.response.PaginationResponse;
import com.shoestore.Server.dto.response.OverviewCartItemResponse;
import com.shoestore.Server.exception.AuthenticationException;
import com.shoestore.Server.security.JwtUtils;
import com.shoestore.Server.service.CartItemService;
import com.shoestore.Server.service.CartService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
  private final CartItemService cartItemService;
  private final CartService cartService;
  private final JwtUtils jwtUtil;

  private int getCurrentUserId(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new AuthenticationException("No JWT token found in request headers") {};
    }
    String token = authHeader.substring(7);
    Claims claims = jwtUtil.parseClaims(token)
            .orElseThrow(() -> new AuthenticationException("Invalid JWT token") {});
    Map<String, Object> userClaim = (Map<String, Object>) claims.get("user");
    Integer userId = (Integer) userClaim.get("id");
    if (userId == null) {
      throw new AuthenticationException("User ID not found in JWT") {};
    }
    return userId;
  }

  @GetMapping
  public ResponseEntity<?> getCart(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "3") int pageSize,
          HttpServletRequest request) {
    try {
      int userId = getCurrentUserId(request);
      return ResponseEntity.ok(cartItemService.getCartItemsByCartId(userId, page, pageSize));
    } catch (AuthenticationException e) {
      Map<String, Object> errorResponse = new HashMap<>();
      errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());
      errorResponse.put("message", "Unauthorized: Token is invalid or expired");
      errorResponse.put("error", e.getMessage());
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  // Tương tự cập nhật các phương thức khác để truyền HttpServletRequest
  @PostMapping("/item/add")
  public ResponseEntity<CartItemDTO> addCartItem(@Valid @RequestBody CartItemDTO cartItemDTO, HttpServletRequest request) {
    int userId = getCurrentUserId(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(cartItemService.addCartItem(userId, cartItemDTO));
  }

  @GetMapping("/item/{id}")
  public ResponseEntity<CartItemDTO> getCartItemById(@PathVariable int id, HttpServletRequest request) {
    int userId = getCurrentUserId(request);
    return ResponseEntity.ok(cartItemService.getCartItemById(userId, id));
  }

  @PutMapping("/item/update-quantity/{id}/{quantity}")
  public ResponseEntity<CartItemDTO> updateCartItem(@PathVariable int id, @PathVariable int quantity, HttpServletRequest request) {
    int userId = getCurrentUserId(request);
    return ResponseEntity.ok(cartItemService.updateQuantity(userId, id, quantity));
  }

  @DeleteMapping("/item/delete/{id}")
  public ResponseEntity<Void> deleteCartItem(@PathVariable int id, HttpServletRequest request) {
    int userId = getCurrentUserId(request);
    cartItemService.deleteCartItem(userId, id);
    return ResponseEntity.ok().build();
  }
}