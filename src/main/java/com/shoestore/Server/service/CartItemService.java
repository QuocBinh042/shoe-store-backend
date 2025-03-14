package com.shoestore.Server.service;


import com.shoestore.Server.dto.request.CartItemDTO;
import com.shoestore.Server.dto.response.CartItemResponse;
import com.shoestore.Server.dto.response.PaginationResponse;
import com.shoestore.Server.entities.CartItem;

public interface CartItemService {
  PaginationResponse<CartItemResponse> getCartItemsByCartId(int cartId, int page, int pageSize);
  CartItemDTO addCartItem(CartItemDTO cartItemDTO);
  CartItemDTO getCartItemById(int id);
  CartItemDTO updateQuantity(int id,int quantityUpdate);
  void deleteCartItem(int id);
}

