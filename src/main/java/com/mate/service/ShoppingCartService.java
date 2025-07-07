package com.mate.service;

import com.mate.dto.cartitem.CartItemResponseDto;
import com.mate.dto.shoppingcart.ShoppingCartResponseDto;
import com.mate.model.User;

public interface ShoppingCartService {

    ShoppingCartResponseDto getByUser(User user);

    CartItemResponseDto addCartItem(User user, Long bookId, int quantity);

    CartItemResponseDto updateCartItemQuantity(User user, Long cartItemId, int quantity);

    void deleteCartItem(User user, Long cartItemId);

}
