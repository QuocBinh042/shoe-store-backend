package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.request.CartItemDTO;
import com.shoestore.Server.dto.response.OverviewCartItemResponse;
import com.shoestore.Server.dto.response.PaginationResponse;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final ProductDetailRepository productDetailRepository;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final CartItemMapper cartItemMapper;
    private final ProductDetailMapper productDetailMapper;
    private final PromotionService promotionService;
    private final ProductMapper productMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public PaginationResponse<OverviewCartItemResponse> getCartItemsByCartId(int userId, int page, int pageSize) {
        log.info("Fetching cart items for userId: {}", userId);
        String key = "cart:user:" + userId;
        Map<Object, Object> cart = redisTemplate.opsForHash().entries(key);
        if (cart.isEmpty()) {
            loadCartFromDatabase(userId);
            cart = redisTemplate.opsForHash().entries(key);
        }
        List<OverviewCartItemResponse> items = cart.values().stream()
                .map(obj -> (CartItemDTO) obj)
                .map(dto -> {
                    ProductDetail productDetail = productDetailRepository.findById(dto.getProductDetail().getProductDetailID())
                            .orElseThrow(() -> new EntityNotFoundException("ProductDetail not found"));
                    Product product = productDetail.getProduct();

                    OverviewCartItemResponse response = new OverviewCartItemResponse();
                    response.setCartItemDTO(dto);
                    response.setProductDetailDTO(productDetailMapper.toProductDetailsResponse(productDetail));
                    response.setProductDTO(productMapper.toDto(product));
                    response.setDiscountPrice(promotionService.getDiscountedPrice(product.getProductID()));

                    Promotion promotion = product.getPromotion();
                    if (promotion != null) {
                        response.setPromotion(promotionService.getPromotionById(promotion.getPromotionID()));
                    } else {
                        response.setPromotion(null);
                    }

                    return response;
                })
                .skip((page - 1L) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());

        return PaginationResponse.<OverviewCartItemResponse>builder()
                .items(items)
                .totalElements(cart.size())
                .totalPages((int) Math.ceil((double) cart.size() / pageSize))
                .currentPage(page)
                .pageSize(pageSize)
                .build();
    }

    @Override
    public CartItemDTO addCartItem(int userId, CartItemDTO cartItemDTO) {
        log.info("Adding new cart item for userId: {}", userId);
        String key = "cart:user:" + userId;
        ProductDetail productDetail = productDetailRepository.findById(cartItemDTO.getProductDetail().getProductDetailID())
                .orElseThrow(() -> new IllegalArgumentException("ProductDetail not found"));
        if (cartItemDTO.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        Map<Object, Object> cart = redisTemplate.opsForHash().entries(key);
        Cart cartel = cartRepository.findCartByUserId(userId);

        CartItemDTO existingItem = null;
        for (Object value : cart.values()) {
            CartItemDTO item = (CartItemDTO) value;
            if (item.getProductDetail().getProductDetailID() == cartItemDTO.getProductDetail().getProductDetailID()) {
                existingItem = item;
                break;
            }
        }

        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + cartItemDTO.getQuantity();
            existingItem.setQuantity(newQuantity);
            redisTemplate.opsForHash().put(key, String.valueOf(existingItem.getCartItemID()), existingItem);

            CartItem cartItem = cartItemRepository.findByCartIdAndProductDetailId(cartel.getCartID(), existingItem.getProductDetail().getProductDetailID())
                    .orElseThrow(() -> new EntityNotFoundException("CartItem not found"));
            cartItem.setQuantity(newQuantity);
            cartItemRepository.save(cartItem);
            log.info("Updated quantity for existing cart item: {}", existingItem);
            return existingItem;
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cartel);
            cartItem.setProductDetail(productDetail);
            cartItem.setQuantity(cartItemDTO.getQuantity());
            CartItem savedCartItem = cartItemRepository.save(cartItem);
            CartItemDTO item = cartItemMapper.toCartItemDTO(savedCartItem);
            redisTemplate.opsForHash().put(key, String.valueOf(item.getCartItemID()), item);
            log.info("Created new cart item: {}", item);
            redisTemplate.expire(key, 24, TimeUnit.HOURS); // TTL 24h
            return item;
        }
    }




    @Override
    public CartItemDTO getCartItemById(int userId, int cartItemId) {
        log.info("Fetching cart item by id: {} for userId: {}", cartItemId, userId);
        String key = "cart:user:" + userId;
        Map<Object, Object> cart = redisTemplate.opsForHash().entries(key);
        CartItemDTO cartItemDTO = cart.values().stream()
                .map(obj -> (CartItemDTO) obj)
                .filter(dto -> dto.getCartItemID() == cartItemId)
                .findFirst()
                .orElse(null);

        if (cartItemDTO == null) {
            CartItem cartItem = cartItemRepository.findById(cartItemId)
                    .orElseThrow(() -> new EntityNotFoundException("CartItem not found with id: " + cartItemId));
            if (cartItem.getCart().getUser().getUserID() != userId) {
                throw new SecurityException("Unauthorized access to cart item");
            }
            cartItemDTO = cartItemMapper.toCartItemDTO(cartItem);
            redisTemplate.opsForHash().put(key, String.valueOf(cartItemDTO.getCartItemID()), cartItemDTO);
            redisTemplate.expire(key, 24, TimeUnit.HOURS);
        }

        return cartItemDTO;
    }

    @Override
    public CartItemDTO updateQuantity(int userId, int cartItemId, int quantity) {
        log.info("Updating cart item id: {} with new quantity: {} for userId: {}", cartItemId, quantity, userId);
        String key = "cart:user:" + userId;
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        Map<Object, Object> cart = redisTemplate.opsForHash().entries(key);
        CartItemDTO cartItemDTO = cart.values().stream()
                .map(obj -> (CartItemDTO) obj)
                .filter(dto -> dto.getCartItemID() == cartItemId)
                .findFirst()
                .orElse(null);

        if (cartItemDTO == null) {
            CartItem cartItem = cartItemRepository.findById(cartItemId)
                    .orElseThrow(() -> new EntityNotFoundException("CartItem not found with id: " + cartItemId));
            if (cartItem.getCart().getUser().getUserID() != userId) {
                throw new SecurityException("Unauthorized access to cart item");
            }
            cartItemDTO = cartItemMapper.toCartItemDTO(cartItem);
        }
        cartItemDTO.setQuantity(quantity);
        redisTemplate.opsForHash().put(key, String.valueOf(cartItemDTO.getCartItemID()), cartItemDTO);
        redisTemplate.expire(key, 24, TimeUnit.HOURS);
        Cart cartel = cartRepository.findCartByUserId(userId);
        CartItem cartItem1 = cartItemRepository.findByCartIdAndProductDetailId(cartel.getCartID(), cartItemDTO.getProductDetail().getProductDetailID()).get();
        cartItem1.setQuantity(cartItemDTO.getQuantity());
        cartItemRepository.save(cartItem1);
        log.info("Cart item updated successfully: {}", cartItemDTO);
        return cartItemDTO;
    }

    @Override
    public void deleteCartItem(int userId, int cartItemId) {
        log.info("Deleting cart item with id: {} for userId: {}", cartItemId, userId);
        String key = "cart:user:" + userId;
        Map<Object, Object> cart = redisTemplate.opsForHash().entries(key);
        CartItemDTO cartItemDTO = cart.values().stream()
                .map(obj -> (CartItemDTO) obj)
                .filter(dto -> dto.getCartItemID() == cartItemId)
                .findFirst()
                .orElse(null);

        if (cartItemDTO != null) {
            redisTemplate.opsForHash().delete(key, String.valueOf(cartItemDTO.getCartItemID()));
            Cart cartel = cartRepository.findCartByUserId(userId);
            CartItem cartItem1 = cartItemRepository.findByCartIdAndProductDetailId(cartel.getCartID(), cartItemDTO.getProductDetail().getProductDetailID())
                    .orElseThrow(() -> new EntityNotFoundException("CartItem not found for delete"));
            cartItemRepository.delete(cartItem1);
            log.info("Cart item deleted successfully: {}", cartItemId);
        } else {
            CartItem cartItem = cartItemRepository.findById(cartItemId)
                    .orElseThrow(() -> new EntityNotFoundException("CartItem not found with id: " + cartItemId));
            if (cartItem.getCart().getUser().getUserID() != userId) {
                throw new SecurityException("Unauthorized access to cart item");
            }
            cartItemRepository.delete(cartItem);
            log.info("Cart item deleted successfully from DB only: {}", cartItemId);
        }

    }

//    public void saveCartToDatabase(int userId) {
//        log.info("Saving cart to database for userId: {}", userId);
//        String key = "cart:user:" + userId;
//        Map<Object, Object> cart = redisTemplate.opsForHash().entries(key);
//
//        Cart cartEntity = cartRepository.findCartByUserId(userId);
//        if (cartEntity == null) {
//            cartEntity = new Cart();
//            cartEntity.setUserId(userId);
//            cartEntity = cartRepository.save(cartEntity);
//        }
//
//        for (Map.Entry<Object, Object> entry : cart.entrySet()) {
//            CartItemDTO item = (CartItemDTO) entry.getValue();
//            ProductDetail productDetail = productDetailRepository.findById(item.getProductDetail().getProductDetailID())
//                    .orElseThrow(() -> new EntityNotFoundException("ProductDetail not found"));
//
//            CartItem cartItem = new CartItem();
//            cartItem.setCart(cartEntity);
//            cartItem.setProductDetail(productDetail);
//            cartItem.setQuantity(item.getQuantity());
//            cartItemRepository.save(cartItem);
//        }
//    }

    public void loadCartFromDatabase(int userId) {
        log.info("Loading cart from database for userId: {}", userId);
        String key = "cart:user:" + userId;

        Cart cart = cartRepository.findCartByUserId(userId);
        if (cart == null) {
            return;
        }

        List<CartItem> cartItems = cartItemRepository.findCartItemsByCartId(cart.getCartID());
        for (CartItem cartItem : cartItems) {
            CartItemDTO item = cartItemMapper.toCartItemDTO(cartItem);
            redisTemplate.opsForHash().put(key, String.valueOf(item.getCartItemID()), item);
        }
        redisTemplate.expire(key, 24, TimeUnit.HOURS);
    }
}