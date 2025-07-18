package com.mate.repository;

import com.mate.exception.EntityNotFoundException;
import com.mate.model.ShoppingCart;
import com.mate.model.User;
import com.mate.repository.shoppingcart.ShoppingCartRepository;
import com.mate.repository.user.UserRepository;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShoppingCartRepositoryTest {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Find shopping card by user")
    @Sql(scripts = {"classpath:database/add-books-and-user-to-shopping-cart.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/remove-books-and-user.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findShoppingCartByUser_withExistingUser_returnsShoppingCart() {
        User user = userRepository.findByEmail("anna.kowalska@example.com").orElseThrow(
                () -> new EntityNotFoundException("User not found"));

        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find shopping cart by user: " + user.getEmail()));

        List<String> titles = shoppingCart.getCartItems().stream()
                        .map(cartItem -> cartItem.getBook().getTitle())
                                .toList();

        Assertions.assertNotNull(shoppingCart);
        Assertions.assertEquals(2, shoppingCart.getCartItems().size());
        Assertions.assertTrue(titles.contains("First book"));
        Assertions.assertTrue(titles.contains("Second book"));
    }

    @Test
    @DisplayName("Find shopping card by user with non existing user")
    @Sql(scripts = {"classpath:database/add-books-and-user-to-shopping-cart.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/remove-books-and-user.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findShoppingCartByUser_withNoExistingUser_throwsException() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            userRepository.findByEmail("kasia.kowalska@example.com").orElseThrow(
                    () -> new EntityNotFoundException("User not found"));
        });
    }
}
