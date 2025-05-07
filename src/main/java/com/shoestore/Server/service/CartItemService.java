package com.shoestore.Server.service;


import com.shoestore.Server.dto.request.CartItemDTO;
import com.shoestore.Server.dto.response.OverviewCartItemResponse;
import com.shoestore.Server.dto.response.PaginationResponse;

public interface CartItemService {
  PaginationResponse<OverviewCartItemResponse> getCartItemsByCartId(int cartId, int page, int pageSize);
  CartItemDTO addCartItem(CartItemDTO cartItemDTO);
  CartItemDTO getCartItemById(int id);
  CartItemDTO updateQuantity(int id,int quantityUpdate);
  void deleteCartItem(int id);
}

