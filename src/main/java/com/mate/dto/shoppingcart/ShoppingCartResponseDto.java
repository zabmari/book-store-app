package com.mate.dto.shoppingcart;

import com.mate.dto.cartitem.CartItemResponseDto;
import java.util.Set;
import lombok.Data;

@Data
public class ShoppingCartResponseDto {
    private long id;
    private long userId;
    private Set<CartItemResponseDto> cartItems;
}
