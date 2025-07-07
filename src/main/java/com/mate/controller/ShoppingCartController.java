package com.mate.controller;

import com.mate.dto.cartitem.CartItemRequestDto;
import com.mate.dto.cartitem.CartItemResponseDto;
import com.mate.dto.cartitem.UpdateCartItemRequestDto;
import com.mate.dto.shoppingcart.ShoppingCartResponseDto;
import com.mate.model.User;
import com.mate.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping Cart", description = "Endpoints for managing shopping cart")
@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get user shopping cart", description = "Get user shopping cart")
    public ShoppingCartResponseDto getCart(@AuthenticationPrincipal User user) {
        return shoppingCartService.getByUser(user);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Operation(summary = "Add book to shopping cart", description = "Add book to shopping cart")
    public CartItemResponseDto addBookToCart(@AuthenticationPrincipal User user,
                                             @Valid @RequestBody CartItemRequestDto requestDto) {
        return shoppingCartService.addCartItem(user,
                requestDto.getBookId(), requestDto.getQuantity());
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Update book quantity", description = "Update book quantity")
    public CartItemResponseDto updateQuantity(@AuthenticationPrincipal User user,
                                              @PathVariable Long cartItemId,
                                              @Valid @RequestBody
                                              UpdateCartItemRequestDto requestDto) {
        return shoppingCartService.updateCartItemQuantity(
                user, cartItemId, requestDto.getQuantity());
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/cart-items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete book from shopping cart",
            description = "Delete book from shopping cart")
    public void deleteBook(@AuthenticationPrincipal User user, @PathVariable Long cartItemId) {
        shoppingCartService.deleteCartItem(user, cartItemId);
    }
}
