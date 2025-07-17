package com.mate.repository;

import com.mate.model.Book;
import com.mate.repository.book.BookRepository;
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
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find all books for given existing category id")
    @Sql(scripts = {"classpath:database/add-books-and-categories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/remove-books-and-categories.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategoriesId_withExistingId_returnsBooks() {
        List<Book> bookList = bookRepository.findAllByCategories_Id(1L);
        List<String> titles = bookList.stream()
                        .map(Book::getTitle)
                                .toList();
        Assertions.assertEquals(2, bookList.size());
        Assertions.assertTrue(titles.contains("First book"));
        Assertions.assertTrue(titles.contains("Third book"));
    }

    @Test
    @DisplayName("Find no books for given no existing category id")
    @Sql(scripts = {"classpath:database/add-books-and-categories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/remove-books-and-categories.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategoriesId_withNoExistingId_returnsEmptyList() {
        List<Book> bookList = bookRepository.findAllByCategories_Id(10L);
        Assertions.assertTrue(bookList.isEmpty());
    }
}
