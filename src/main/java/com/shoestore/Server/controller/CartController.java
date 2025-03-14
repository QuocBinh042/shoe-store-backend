package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.CartDTO;
import com.shoestore.Server.dto.request.CartItemDTO;
import com.shoestore.Server.dto.request.ProductDetailDTO;
import com.shoestore.Server.dto.response.PaginationResponse;
import com.shoestore.Server.dto.response.CartItemResponse;
import com.shoestore.Server.service.CartItemService;
import com.shoestore.Server.service.CartService;

import com.shoestore.Server.service.ProductDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class CartController {
  private final CartItemService cartItemService;
  private final CartService cartService;
  private final ProductDetailService productDetailService;
  public CartController(CartItemService cartItemService, CartService cartService, ProductDetailService productDetailService) {
    this.cartItemService = cartItemService;
    this.cartService = cartService;
      this.productDetailService = productDetailService;
  }

  @GetMapping("/cart/{id}")
  public ResponseEntity<CartDTO> getCartByCartId(@PathVariable int id) {
    return ResponseEntity.ok(cartService.getCartByUserId(id));
  }

  @GetMapping("/cart-item/by-cart-id/{id}")
  public ResponseEntity<PaginationResponse<CartItemResponse>> getCartItemsByCartId(
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
    System.out.println("Được goi");
    return ResponseEntity.ok(cartItemService.updateQuantity(id, quantity));
  }

  @DeleteMapping("/cart-item/delete/{id}")
  public ResponseEntity<Void> deleteCartItem(@PathVariable int id) {
    cartItemService.deleteCartItem(id);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/cart-item/add")
  public ResponseEntity<CartItemDTO> addCartItem(@RequestBody CartItemDTO cartItemDTO) {
    return ResponseEntity.ok(cartItemService.addCartItem(cartItemDTO));
  }
}

