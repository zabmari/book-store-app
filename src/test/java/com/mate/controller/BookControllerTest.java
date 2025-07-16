package com.mate.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mate.dto.book.BookDto;
import com.mate.dto.book.CreateBookRequestDto;
import com.mate.dto.book.UpdateBookRequestDto;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class BookControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
                    new ClassPathResource("database/add-books-and-categories.sql")
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
                    new ClassPathResource("database/remove-books-and-categories.sql")
            );
        }
    }

    @Test
    @DisplayName("Get all books")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAll_ValidPageable_ReturnAllBooks() throws Exception {

        BookDto book1 = new BookDto(1L, "First book","First author","978-0-123456-47-1",
                new BigDecimal("39.90"),"first description","img1.jpg",Set.of(1L));

        BookDto book2 = new BookDto(2L, "Second book", "Second author", "978-0-123456-47-2",
                new BigDecimal("29.90"), "second description", "img2.jpg", Set.of(2L));

        BookDto book3 = new BookDto(3L, "Third book", "Third author", "978-0-123456-47-3",
                new BigDecimal("19.90"), "third description", "img3.jpg", Set.of(1L));

        List<BookDto> expected = new ArrayList<>();

        expected.add(book1);
        expected.add(book2);
        expected.add(book3);

        MvcResult result = mockMvc.perform(
                get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), BookDto[].class);
        Assertions.assertEquals(3, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @DisplayName("Find book by id with exist book")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getBookById_ValidId_ReturnBookDto() throws Exception {
        Long bookId = 1L;

        BookDto book1 = new BookDto(1L, "First book","First author","978-0-123456-47-1",
                new BigDecimal("39.90"),"first description","img1.jpg",Set.of(1L));

        MvcResult result = mockMvc.perform(
                        get("/books/{id}", bookId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), BookDto.class);

        assertThat(actual).isEqualTo(book1);
    }

    @Test
    @DisplayName("Find book by id with non-existent id")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getBookById_inValidId_ReturnBookDto() throws Exception {
        Long bookId = 99L;

        mockMvc.perform(
                        get("/books/{id}", bookId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Create new book")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void create_ValidCreateBookRequestDto_ReturnsBookDto() throws Exception {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto(
                "New book", "New author", "978-0-123456-47-7",
                new BigDecimal("39.90"), "first description", "img1.jpg", Set.of(1L, 2L));

        String jsonRequest = objectMapper.writeValueAsString(createBookRequestDto);

        MvcResult result = mockMvc.perform(
                        post("/books")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)
                )
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getAuthor()).isEqualTo(createBookRequestDto.getAuthor());
        assertThat(actual.getTitle()).isEqualTo(createBookRequestDto.getTitle());
        assertThat(actual.getIsbn()).isEqualTo(createBookRequestDto.getIsbn());
        assertThat(actual.getPrice()).isEqualTo(createBookRequestDto.getPrice());
        assertThat(actual.getDescription()).isEqualTo(createBookRequestDto.getDescription());
        assertThat(actual.getCoverImage()).isEqualTo(createBookRequestDto.getCoverImage());
        assertThat(actual.getCategoriesIds()).isEqualTo(createBookRequestDto.getCategoriesIds());
    }

    @Test
    @DisplayName("Delete book by id")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void delete_ValidId_DeletesBook() throws Exception {
        Long bookId = 1L;

        mockMvc.perform(
                        delete("/books/{id}", bookId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @DisplayName("Update book by id with existent id")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateBookById_ValidId_ReturnBookDto() throws Exception {
        Long bookId = 1L;

        UpdateBookRequestDto updateBookRequestDto = new UpdateBookRequestDto(
                "Update book", "Update author", "978-0-123456-47-1",
                BigDecimal.valueOf(39.90), "Update description", "img1.jpg", Set.of(1L)
        );

        BookDto bookDto = new BookDto(
                bookId,
                updateBookRequestDto.getTitle(),
                updateBookRequestDto.getAuthor(),
                updateBookRequestDto.getIsbn(),
                updateBookRequestDto.getPrice(),
                updateBookRequestDto.getDescription(),
                updateBookRequestDto.getCoverImage(),
                updateBookRequestDto.getCategoriesIds()
        );

        String jsonRequest = objectMapper.writeValueAsString(updateBookRequestDto);

        MvcResult result = mockMvc.perform(
                        put("/books/{id}", bookId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        assertThat(actual).isEqualTo(bookDto);
    }
}
