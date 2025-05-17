package com.shoestore.Server.repositories;

import com.shoestore.Server.entities.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

  @Query("SELECT ci FROM CartItem ci WHERE ci.cart.cartID = :cartId")
  List<CartItem> findCartItemsByCartId(@Param("cartId") int cartId);
  @Query("SELECT ci FROM CartItem ci WHERE ci.cart.cartID = :cartId AND ci.productDetail.productDetailID = :productDetailId")
  Optional<CartItem> findByCartIdAndProductDetailId(@Param("cartId") int cartId, @Param("productDetailId") int productDetailId);

}

