package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.request.CartItemDTO;
import com.shoestore.Server.dto.response.OverviewCartItemResponse;
import com.shoestore.Server.dto.response.PaginationResponse;
import com.shoestore.Server.dto.response.SearchProductResponse;
import com.shoestore.Server.entities.*;
import com.shoestore.Server.mapper.CartItemMapper;
import com.shoestore.Server.mapper.ProductDetailMapper;
import com.shoestore.Server.mapper.ProductMapper;
import com.shoestore.Server.repositories.CartItemRepository;
import com.shoestore.Server.repositories.CartRepository;
import com.shoestore.Server.repositories.ProductDetailRepository;
import com.shoestore.Server.service.CartItemService;
import com.shoestore.Server.service.PromotionService;
import com.shoestore.Server.service.PaginationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CartItemServiceImpl implements CartItemService {
    private final ProductDetailRepository productDetailRepository;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final PaginationService paginationService;
    private final CartItemMapper cartItemMapper;
    private final ProductDetailMapper productDetailMapper;
    private final PromotionService promotionService;
    private final ProductMapper productMapper;

    public CartItemServiceImpl(ProductDetailRepository productDetailRepository, CartItemRepository cartItemRepository, CartRepository cartRepository, PaginationService paginationService, CartItemMapper cartItemMapper, ProductDetailMapper productDetailMapper, PromotionService promotionService, ProductMapper productMapper) {
        this.productDetailRepository = productDetailRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.paginationService = paginationService;
        this.cartItemMapper = cartItemMapper;
        this.productDetailMapper = productDetailMapper;
        this.promotionService = promotionService;
        this.productMapper = productMapper;
    }

    @Override
    public PaginationResponse<OverviewCartItemResponse> getCartItemsByCartId(int cartId, int page, int pageSize) {
        log.info("Fetching cart items for cart id: {}", cartId);
        Pageable pageable = paginationService.createPageable(page, pageSize);
        Page<CartItem> cartItemsPage = cartItemRepository.findCartItemsByCartId(cartId, pageable);

        return paginationService.paginate(cartItemsPage.map(cartItem -> {
            ProductDetail productDetail = cartItem.getProductDetail();
            Product product = productDetail.getProduct();

            OverviewCartItemResponse cartItemResponse = new OverviewCartItemResponse();
            cartItemResponse.setCartItemDTO(cartItemMapper.toCartItemDTO(cartItem));
            cartItemResponse.setProductDetailDTO(productDetailMapper.toDto(productDetail));
            cartItemResponse.setProductDTO(productMapper.toDto(productDetail.getProduct()));
            cartItemResponse.setDiscountPrice(promotionService.getDiscountedPrice(product.getProductID()));

            Promotion promotion = product.getPromotion();
            if (promotion != null) {
                cartItemResponse.setPromotion(promotionService.getPromotionById(promotion.getPromotionID()));
            } else {
                cartItemResponse.setPromotion(null);
            }

            return cartItemResponse;
        }));
    }


    @Override
    public CartItemDTO addCartItem(CartItemDTO cartItemDTO) {
        log.info("Adding new cart item: {}", cartItemDTO);

        Cart cart = cartRepository.findById(cartItemDTO.getCart().getCartID())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        ProductDetail productDetail = productDetailRepository.findById(cartItemDTO.getProductDetail().getProductDetailID())
                .orElseThrow(() -> new IllegalArgumentException("ProductDetail not found"));

        Optional<CartItem> existingCartItemOpt = cartItemRepository.findByCartIdAndProductDetailId(cart.getCartID(), productDetail.getProductDetailID());

        CartItem cartItem;

        if (existingCartItemOpt.isPresent()) {
            cartItem = existingCartItemOpt.get();
            cartItem.setQuantity(cartItem.getQuantity() + cartItemDTO.getQuantity());
            log.info("Updated quantity for existing cart item: {}", cartItem);
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProductDetail(productDetail);
            cartItem.setQuantity(cartItemDTO.getQuantity());
            log.info("Created new cart item: {}", cartItem);
        }

        return cartItemMapper.toCartItemDTO(cartItemRepository.save(cartItem));
    }


    @Override
    public CartItemDTO getCartItemById(int id) {
        log.info("Fetching cart item by id: {}", id);
        return cartItemRepository.findById(id)
                .map(cartItemMapper::toCartItemDTO)
                .orElseThrow(() -> {
                    log.warn("CartItem not found with id: {}", id);
                    return new EntityNotFoundException("CartItem not found with id: " + id);
                });
    }

    @Override
    public CartItemDTO updateQuantity(int id, int quantity) {
        log.info("Updating cart item id: {} with new quantity: {}", id, quantity);
        CartItem existCartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CartItem not found with id: " + id));

        existCartItem.setQuantity(quantity);

        log.info("Cart item updated successfully: {}", existCartItem);
        return cartItemMapper.toCartItemDTO(cartItemRepository.save(existCartItem));
    }

    @Override
    public void deleteCartItem(int id) {
        log.info("Deleting cart item with id: {}", id);
        if (!cartItemRepository.existsById(id)) {
            log.warn("Cart item not found with id: {}", id);
            throw new EntityNotFoundException("Cart item not found with id: " + id);
        }
        cartItemRepository.deleteById(id);
        log.info("Cart item deleted successfully: {}", id);
    }
}
