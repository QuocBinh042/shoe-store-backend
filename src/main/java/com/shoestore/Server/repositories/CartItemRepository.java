package com.shoestore.Server.repositories;

import com.shoestore.Server.entities.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

  @Query("SELECT ci FROM CartItem ci WHERE ci.cart.cartID = :cartId")
  Page<CartItem> findCartItemsByCartId(@Param("cartId") int cartId, Pageable pageable);

}

