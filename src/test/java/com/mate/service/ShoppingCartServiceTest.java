package com.mate.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mate.dto.cartitem.CartItemResponseDto;
import com.mate.dto.shoppingcart.ShoppingCartResponseDto;
import com.mate.exception.EntityNotFoundException;
import com.mate.mapper.CartItemMapper;
import com.mate.mapper.ShoppingCartMapper;
import com.mate.model.Book;
import com.mate.model.CartItem;
import com.mate.model.Category;
import com.mate.model.ShoppingCart;
import com.mate.model.User;
import com.mate.repository.book.BookRepository;
import com.mate.repository.cartitem.CartItemRepository;
import com.mate.repository.shoppingcart.ShoppingCartRepository;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @Mock
    private CartItemMapper cartItemMapper;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Test
    @DisplayName("Find shopping cart by existing user")
    public void getByUser_ExistingUser_ReturnsShoppingCartDto() {
        User user = createUser();
        Book book = createBook();
        ShoppingCart shoppingCart = createShoppingCart(user);
        CartItem cartItem = createCartItem(shoppingCart, book, 1);
        shoppingCart.setCartItems(Set.of(cartItem));

        CartItemResponseDto cartItemResponseDto = createCartItemResponse(cartItem);
        ShoppingCartResponseDto cartResponseDto = createShoppingCartResponseDto(
                shoppingCart, Set.of(cartItemResponseDto));

        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.of(shoppingCart));
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(cartResponseDto);

        ShoppingCartResponseDto foundCart = shoppingCartService.getByUser(user);
        assertThat(foundCart).isEqualTo(cartResponseDto);
    }

    @Test
    @DisplayName("Find shopping cart with non-existing shopping cart throws exception")
    public void getByUser_NonExistingUser_ThrowsException() {
        User user = new User();
        user.setEmail("nonexistent@example.com");

        when(shoppingCartRepository.findByUser(any(User.class))).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            shoppingCartService.getByUser(user);
        });
        String expected = "Can't find shopping cart by user: " + user.getEmail();
        String actual = exception.getMessage();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Add quantity to existing cart item")
    public void addCartItem_ExistingCartItem_ReturnsCartItemResponseDto() {
        Long bookId = 1L;
        int quantityToAdd = 2;

        User user = createUser();
        Book book = createBook();
        ShoppingCart shoppingCart = createShoppingCart(user);
        CartItem cartItem = createCartItem(shoppingCart, book, 1);
        CartItem updatedItem = new CartItem(1L, shoppingCart, book, 1 + quantityToAdd);
        shoppingCart.setCartItems(Set.of(cartItem));
        CartItemResponseDto cartItemResponseDto = createCartItemResponse(updatedItem);

        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.of(shoppingCart));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(cartItemRepository.findByBookAndShoppingCart(book, shoppingCart)).thenReturn(
                Optional.of(cartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(updatedItem);
        when(cartItemMapper.toDto(updatedItem)).thenReturn(cartItemResponseDto);

        CartItemResponseDto result = shoppingCartService.addCartItem(user, bookId, quantityToAdd);
        assertThat(result).isEqualTo(cartItemResponseDto);
    }

    @Test
    @DisplayName("Add new cart item with non-existing cart item")
    public void addCartItem_NonExistingCartItem_ReturnsCartItemResponseDto() {
        Long bookId = 1L;
        int quantity = 2;

        User user = createUser();
        Book book = createBook();
        ShoppingCart shoppingCart = createShoppingCart(user);
        CartItem cartItem = createCartItem(shoppingCart, book, quantity);

        CartItemResponseDto cartItemResponseDto = createCartItemResponse(cartItem);

        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.of(shoppingCart));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(cartItemRepository.findByBookAndShoppingCart(book, shoppingCart)).thenReturn(
                Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(cartItemMapper.toDto(cartItem)).thenReturn(cartItemResponseDto);

        CartItemResponseDto result = shoppingCartService.addCartItem(user, bookId, quantity);

        assertThat(result).isEqualTo(cartItemResponseDto);
    }

    @Test
    @DisplayName("Update quantity of cart item")
    public void updateCartItemQuantity_ExistingCartItem_ReturnsCartItemResponseDto() {
        Long cartItemId = 1L;
        int quantity = 2;

        User user = createUser();
        Book book = createBook();
        ShoppingCart shoppingCart = createShoppingCart(user);
        CartItem cartItem = createCartItem(shoppingCart, book, 1);

        CartItemResponseDto cartItemResponseDto = createCartItemResponse(cartItem);
        cartItemResponseDto.setQuantity(quantity);

        when(cartItemRepository.findByIdAndShoppingCartUser(cartItemId, user))
                .thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        when(cartItemMapper.toDto(cartItem)).thenReturn(cartItemResponseDto);

        CartItemResponseDto result = shoppingCartService.updateCartItemQuantity(
                user, cartItemId, quantity);

        assertThat(result).isEqualTo(cartItemResponseDto);
        assertThat(cartItem.getQuantity()).isEqualTo(quantity);
        verify(cartItemRepository).save(cartItem);
    }

    @Test
    @DisplayName("Delete cart item")
    public void deleteCartItem_ExistingCartItem_DeletesCartItem() {

        User user = createUser();
        Book book = createBook();
        ShoppingCart shoppingCart = createShoppingCart(user);
        CartItem cartItem = createCartItem(shoppingCart, book, 1);

        shoppingCart.getCartItems().add(cartItem);

        when(cartItemRepository.findByIdAndShoppingCartUser(cartItem.getId(), user))
                .thenReturn(Optional.of(cartItem));
        shoppingCartService.deleteCartItem(user, cartItem.getId());

        assertThat(shoppingCart.getCartItems()).doesNotContain(cartItem);
        verify(cartItemRepository).delete(cartItem);
    }

    private User createUser() {
        return new User(1L, "anna.kowalska@example.com",
                "$2a$10$xCH3VTUPwI8pj/NFAaKbxOMbM3r6rbjSnzh3CzazIUs9Gi5WqoVgq", "Anna",
                "Kowalska", "ul. Zielona 15, Warszawa, Polska", null);
    }

    private Book createBook() {
        return new Book(1L, "First book", "First author", "978-0-123456-47-1",
                BigDecimal.valueOf(39.90), "first description", "img1.jpg", false,
                Set.of(new Category(1L)));
    }

    private ShoppingCart createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setId(1L);
        shoppingCart.setCartItems(new HashSet<>());
        return shoppingCart;
    }

    private CartItem createCartItem(ShoppingCart shoppingCart, Book book, int quantity) {
        return new CartItem(1L, shoppingCart, book, quantity);
    }

    private CartItemResponseDto createCartItemResponse(CartItem cartItem) {
        CartItemResponseDto cartItemResponseDto = new CartItemResponseDto();
        cartItemResponseDto.setId(cartItem.getId());
        cartItemResponseDto.setBookId(cartItem.getBook().getId());
        cartItemResponseDto.setBookTitle(cartItem.getBook().getTitle());
        cartItemResponseDto.setQuantity(cartItem.getQuantity());
        return cartItemResponseDto;
    }

    private ShoppingCartResponseDto createShoppingCartResponseDto(
            ShoppingCart shoppingCart, Set<CartItemResponseDto> cartItems) {
        ShoppingCartResponseDto shoppingCartResponseDto = new ShoppingCartResponseDto();
        shoppingCartResponseDto.setId(shoppingCart.getId());
        shoppingCartResponseDto.setUserId(shoppingCart.getUser().getId());
        shoppingCartResponseDto.setCartItems(cartItems);
        return shoppingCartResponseDto;
    }
}
