package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.request.CartDTO;
import com.shoestore.Server.entities.Cart;
import com.shoestore.Server.entities.User;
import com.shoestore.Server.mapper.CartMapper;
import com.shoestore.Server.repositories.CartRepository;
import com.shoestore.Server.repositories.UserRepository;
import com.shoestore.Server.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final UserRepository userRepository;


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
    public CartDTO addCartByRegister(CartDTO cartDTO) {
        Cart cart=cartMapper.toEntity(cartDTO);
        User user = userRepository.findById(cartDTO.getUser().getUserID())
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", cartDTO.getUser().getUserID());
                    return new IllegalArgumentException("User not found");
                });
        cart.setUser(user);
        return cartMapper.toDto(cartRepository.save(cart));
    }
}
