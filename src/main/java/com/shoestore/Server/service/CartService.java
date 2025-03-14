package com.shoestore.Server.service;

import com.shoestore.Server.dto.request.CartDTO;
import com.shoestore.Server.entities.Cart;

public interface CartService {
    CartDTO getCartByUserId(int id);
    CartDTO getCartById(int id);
}
