package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.request.CartDTO;
import com.shoestore.Server.entities.Cart;
import com.shoestore.Server.mapper.CartMapper;
import com.shoestore.Server.repositories.CartRepository;
import com.shoestore.Server.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    public CartServiceImpl(CartRepository cartRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    @Override
    public CartDTO getCartByUserId(int id) {
        log.info("Fetching cart for user id: {}", id);
        Cart cart = cartRepository.findCartByUserId(id);
        if (cart == null) {
            log.warn("No cart found for user id: {}", id);
        }
        return cartMapper.toDto(cart);
    }

    @Override
    public CartDTO getCartById(int id) {
        return cartMapper.toDto(cartRepository.findById(id).orElseThrow());
    }
}
