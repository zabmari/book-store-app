package com.mate.repository.cartitem;

import com.mate.model.Book;
import com.mate.model.CartItem;
import com.mate.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByBookAndShoppingCart(Book book, ShoppingCart shoppingCart);
}
