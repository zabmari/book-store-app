package com.mate.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mate.dto.category.CategoryDto;
import jakarta.transaction.Transactional;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
public class CategoryControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void resetDatabase(@Autowired DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/remove-books-and-categories.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/add-books-and-categories.sql"));
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
    @DisplayName("Create new category")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void create_ValidCreateBookRequestDto_ReturnsBookDto() throws Exception {
        CategoryDto categoryDto = new CategoryDto(null, "new", "new category");
        String jsonRequest = objectMapper.writeValueAsString(categoryDto);

        MvcResult result = mockMvc.perform(
                        post("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)
                )
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo(categoryDto.getName());
        assertThat(actual.getDescription()).isEqualTo(categoryDto.getDescription());
    }

    @Test
    @DisplayName("Get all categories")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAll_ValidPageable_ReturnAllCategories() throws Exception {
        CategoryDto categoryDto1 = new CategoryDto(10L, "first", "first category");
        CategoryDto categoryDto2 = new CategoryDto(11L, "second", "second category");
        List<CategoryDto> expected = new ArrayList<>();

        expected.add(categoryDto1);
        expected.add(categoryDto2);

        MvcResult result = mockMvc.perform(
                        get("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), CategoryDto[].class);
        Assertions.assertEquals(2, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @DisplayName("Get category by id with exist id")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getCategoryById_ValidId_ReturnCategoryDto() throws Exception {
        Long categoryId = 10L;

        CategoryDto category = new CategoryDto(10L, "first", "first category");

        MvcResult result = mockMvc.perform(
                        get("/categories/{id}", categoryId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), CategoryDto.class);

        assertThat(actual).isEqualTo(category);
    }

    @Test
    @DisplayName("Get category by id with non-existent id")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getCategoryById_InvalidId_ReturnCategoryDto() throws Exception {
        Long categoryId = 99L;

        mockMvc.perform(
                        get("/categories/{id}", categoryId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Delete category by id")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void delete_ValidId_DeletesCategories() throws Exception {
        Long categoryId = 10L;

        mockMvc.perform(
                        delete("/categories/{id}", categoryId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();
    }
}
