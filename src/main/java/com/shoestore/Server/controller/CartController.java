package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.CartDTO;
import com.shoestore.Server.dto.request.CartItemDTO;
import com.shoestore.Server.dto.response.PaginationResponse;
import com.shoestore.Server.dto.response.OverviewCartItemResponse;
import com.shoestore.Server.service.CartItemService;
import com.shoestore.Server.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CartController {
  private final CartItemService cartItemService;
  private final CartService cartService;


  @GetMapping("/cart/{id}")
  public ResponseEntity<CartDTO> getCartByCartId(@PathVariable int id) {
    return ResponseEntity.ok(cartService.getCartByUserId(id));
  }
  @PostMapping("/cart/add")
  public ResponseEntity<CartDTO> addCart(@Valid @RequestBody CartDTO cartDTO) {
    return ResponseEntity.ok(cartService.addCartByRegister(cartDTO));
  }
  @GetMapping("/cart-item/by-cart-id/{id}")
  public ResponseEntity<PaginationResponse<OverviewCartItemResponse>> getCartItemsByCartId(
          @PathVariable int id,
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "3") int pageSize) {
    return ResponseEntity.ok(cartItemService.getCartItemsByCartId(id, page, pageSize));
  }

  @GetMapping("/cart-item/{id}")
  public ResponseEntity<CartItemDTO> getCartItemById(@PathVariable int id) {
    return ResponseEntity.ok(cartItemService.getCartItemById(id));
  }

  @PutMapping("/cart-item/update-quantity/{id}/{quantity}")
  public ResponseEntity<CartItemDTO> updateCartItem(@PathVariable int id, @PathVariable("quantity") int quantity) {
    return ResponseEntity.ok(cartItemService.updateQuantity(id, quantity));
  }

  @DeleteMapping("/cart-item/delete/{id}")
  public ResponseEntity<Void> deleteCartItem(@PathVariable int id) {
    cartItemService.deleteCartItem(id);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/cart-item/add")
  public ResponseEntity<CartItemDTO> addCartItem(@Valid @RequestBody CartItemDTO cartItemDTO) {
    return ResponseEntity.ok(cartItemService.addCartItem(cartItemDTO));
  }
}

