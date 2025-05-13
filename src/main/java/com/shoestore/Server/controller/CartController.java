package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.CartItemDTO;
import com.shoestore.Server.dto.response.PaginationResponse;
import com.shoestore.Server.dto.response.OverviewCartItemResponse;
import com.shoestore.Server.exception.AuthenticationException;
import com.shoestore.Server.service.CartItemService;
import com.shoestore.Server.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
  private final CartItemService cartItemService;
  private final CartService cartService;

  private int getCurrentUserId() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof Integer) {
      return (Integer) principal;
    }
    throw new BadCredentialsException("User ID not found in JWT or invalid type");
  }
  @GetMapping
  public ResponseEntity<?> getCart(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "3") int pageSize) {
    try {
      int userId = getCurrentUserId();
      return ResponseEntity.ok(cartItemService.getCartItemsByCartId(userId, page, pageSize));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(e.getMessage());
    }
  }

  @PostMapping("/item/add")
  public ResponseEntity<CartItemDTO> addCartItem(@Valid @RequestBody CartItemDTO cartItemDTO) {
    int userId = getCurrentUserId();
    return ResponseEntity.status(HttpStatus.CREATED).body(cartItemService.addCartItem(userId, cartItemDTO));
  }

  @GetMapping("/item/{id}")
  public ResponseEntity<CartItemDTO> getCartItemById(@PathVariable int id) {
    int userId = getCurrentUserId();
    return ResponseEntity.ok(cartItemService.getCartItemById(userId, id));
  }

  @PutMapping("/item/update-quantity/{id}/{quantity}")
  public ResponseEntity<CartItemDTO> updateCartItem(@PathVariable int id, @PathVariable int quantity) {
    int userId = getCurrentUserId();
    return ResponseEntity.ok(cartItemService.updateQuantity(userId, id, quantity));
  }

  @DeleteMapping("/item/delete/{id}")
  public ResponseEntity<Void> deleteCartItem(@PathVariable int id) {
    int userId = getCurrentUserId();
    cartItemService.deleteCartItem(userId, id);
    return ResponseEntity.ok().build();
  }

  // Lưu giỏ hàng vào DB khi đặt hàng
//  @PostMapping("/checkout")
//  public ResponseEntity<Void> checkout() {
//    int userId = getCurrentUserId();
//    cartItemService.saveCartToDatabase(userId);
//    return ResponseEntity.ok().build();
//  }
}