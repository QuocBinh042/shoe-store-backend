package com.shoestore.Server.service;


import com.shoestore.Server.dto.request.CartItemDTO;
import com.shoestore.Server.dto.response.OverviewCartItemResponse;
import com.shoestore.Server.dto.response.PaginationResponse;

public interface CartItemService {
  PaginationResponse<OverviewCartItemResponse> getCartItemsByCartId(int cartId, int page, int pageSize);
  CartItemDTO addCartItem(int userId, CartItemDTO cartItemDTO);
  CartItemDTO getCartItemById(int userId, int cartItemId);
  CartItemDTO updateQuantity(int userId, int cartItemId, int quantity);
  void deleteCartItem(int userId, int cartItemId);
}

