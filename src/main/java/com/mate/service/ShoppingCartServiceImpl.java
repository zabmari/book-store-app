package com.mate.service;

import com.mate.dto.cartitem.CartItemResponseDto;
import com.mate.dto.shoppingcart.ShoppingCartResponseDto;
import com.mate.exception.EntityNotFoundException;
import com.mate.mapper.CartItemMapper;
import com.mate.mapper.ShoppingCartMapper;
import com.mate.model.Book;
import com.mate.model.CartItem;
import com.mate.model.ShoppingCart;
import com.mate.model.User;
import com.mate.repository.book.BookRepository;
import com.mate.repository.cartitem.CartItemRepository;
import com.mate.repository.shoppingcart.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCartResponseDto getByUser(User user) {
        System.out.println(shoppingCartRepository.findByUser(user));
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find shopping cart by user: " + user.getEmail()));
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public CartItemResponseDto addCartItem(User user, Long bookId, int quantity) {
        ShoppingCart shoppingCart = getOrCreateShoppingCart(user);

        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find book by id: " + bookId));

        CartItem cartItem;
        Optional<CartItem> optionalCartItem =
                cartItemRepository.findByBookAndShoppingCart(book, shoppingCart);
        if (optionalCartItem.isPresent()) {
            CartItem existingItem = optionalCartItem.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItem = cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setShoppingCart(shoppingCart);
            newItem.setBook(book);
            newItem.setQuantity(quantity);
            cartItem = cartItemRepository.save(newItem);
        }
        return cartItemMapper.toDto(cartItem);
    }

    @Override
    public CartItemResponseDto updateCartItemQuantity(User user, Long cartItemId, int quantity) {
        CartItem cartItem = getCartItemForUser(user, cartItemId);
        cartItem.setQuantity(quantity);
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        return cartItemMapper.toDto(savedCartItem);
    }

    @Override
    public void deleteCartItem(User user, Long cartItemId) {
        CartItem cartItem = getCartItemForUser(user, cartItemId);
        ShoppingCart shoppingCart = cartItem.getShoppingCart();
        shoppingCart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
    }

    private ShoppingCart getOrCreateShoppingCart(User user) {
        return shoppingCartRepository.findByUser(user)
                .orElseGet(() -> {
                            ShoppingCart shoppingCart = new ShoppingCart();
                            shoppingCart.setUser(user);
                            return shoppingCartRepository.save(shoppingCart);
                        }
                );
    }

    private CartItem getCartItemForUser(User user, Long cartItemId) {
        return cartItemRepository.findByIdAndShoppingCartUser(cartItemId, user).orElseThrow(
                () -> new EntityNotFoundException("Can't find cart item for user: "
                        + user.getEmail()));
    }
}
