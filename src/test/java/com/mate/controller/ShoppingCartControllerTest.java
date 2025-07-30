package com.mate.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mate.dto.cartitem.CartItemRequestDto;
import com.mate.dto.cartitem.CartItemResponseDto;
import com.mate.dto.cartitem.UpdateCartItemRequestDto;
import com.mate.dto.shoppingcart.ShoppingCartResponseDto;
import com.mate.mapper.ShoppingCartMapper;
import com.mate.model.ShoppingCart;
import com.mate.model.User;
import com.mate.repository.shoppingcart.ShoppingCartRepository;
import com.mate.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class ShoppingCartControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void resetDatabase(@Autowired DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/remove-books-and-user.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/add-books-and-user-to-shopping-cart.sql"));
        }
    }

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .build();
        teardown(dataSource);

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/add-books-and-user-to-shopping-cart.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/remove-books-and-user.sql")
            );
        }
    }

    @Test
    @DisplayName("Find user shopping cart")
    @WithUserDetails("anna.kowalska@example.com")
    public void getShoppingCartByUser_ValidUser_ReturnsShoppingCartDto() throws Exception {

        MvcResult result = mockMvc.perform(
                        get("/cart")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), ShoppingCartResponseDto.class);

        User user = userRepository.findByEmail("anna.kowalska@example.com").orElseThrow();
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user).orElseThrow();
        ShoppingCartResponseDto expected = shoppingCartMapper.toDto(shoppingCart);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    @DisplayName("Increase book quantity in shopping cart")
    @WithUserDetails("anna.kowalska@example.com")
    public void increaseBookQuantityInShoppingCart_ValidBook_ReturnsCartItemResponseDto()
            throws Exception {

        CartItemRequestDto cartItemRequestDto = new CartItemRequestDto();
        cartItemRequestDto.setBookId(10L);
        cartItemRequestDto.setQuantity(2);

        String jsonRequest = objectMapper.writeValueAsString(cartItemRequestDto);

        MvcResult result = mockMvc.perform(
                        post("/cart")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)
                )
                .andExpect(status().isOk())
                .andReturn();

        CartItemResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CartItemResponseDto.class);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getBookId()).isEqualTo(cartItemRequestDto.getBookId());
        assertThat(actual.getQuantity()).isEqualTo(4);
        assertThat(actual.getBookTitle()).isEqualTo("First test book");
    }

    @Test
    @DisplayName("Add new book to shopping cart")
    @WithUserDetails("anna.kowalska@example.com")
    public void addNewBookToShoppingCart_ValidBook_ReturnsCartItemResponseDto() throws Exception {
        CartItemRequestDto cartItemRequestDto = new CartItemRequestDto();
        cartItemRequestDto.setBookId(12L);
        cartItemRequestDto.setQuantity(1);

        String jsonRequest = objectMapper.writeValueAsString(cartItemRequestDto);

        MvcResult result = mockMvc.perform(
                        post("/cart")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)
                )
                .andExpect(status().isOk())
                .andReturn();

        CartItemResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CartItemResponseDto.class);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getBookId()).isEqualTo(cartItemRequestDto.getBookId());
        assertThat(actual.getQuantity()).isEqualTo(cartItemRequestDto.getQuantity());
        assertThat(actual.getBookTitle()).isEqualTo("Third test book");
    }

    @Test
    @DisplayName("Update book quantity in shopping cart")
    @WithUserDetails("anna.kowalska@example.com")
    public void updateBookQuantityInShoppingCart_ValidBook_ReturnsCartItemResponseDto()
            throws Exception {

        Long cartItemId = 10L;
        UpdateCartItemRequestDto updateCartItemRequestDto = new UpdateCartItemRequestDto();
        updateCartItemRequestDto.setQuantity(5);

        String jsonRequest = objectMapper.writeValueAsString(updateCartItemRequestDto);

        MvcResult result = mockMvc.perform(
                        put("/cart/cart-items/{cartItemId}", cartItemId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)
                )
                .andExpect(status().isOk())
                .andReturn();

        CartItemResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CartItemResponseDto.class);

        assertThat(actual.getId()).isEqualTo(cartItemId);
        assertThat(actual.getQuantity()).isEqualTo(updateCartItemRequestDto.getQuantity());
    }
}
