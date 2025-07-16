package com.mate.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mate.dto.book.BookDto;
import com.mate.dto.book.CreateBookRequestDto;
import com.mate.dto.book.UpdateBookRequestDto;
import com.mate.exception.EntityNotFoundException;
import com.mate.mapper.BookMapper;
import com.mate.model.Book;
import com.mate.model.Category;
import com.mate.repository.book.BookRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Save new book")
    public void save_ValidCreateBookRequestDto_ReturnsBookDto() {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto(
                "First book", "First author", "978-0-123456-47-1", BigDecimal.valueOf(39.90),
                "first description", "img1.jpg", Set.of(1L, 2L));

        Book book = new Book(
                null, createBookRequestDto.getTitle(), createBookRequestDto.getAuthor(),
                createBookRequestDto.getIsbn(), createBookRequestDto.getPrice(),
                createBookRequestDto.getDescription(), createBookRequestDto.getCoverImage(),
                false, createBookRequestDto.getCategoriesIds()
                        .stream()
                        .map(Category::new)
                        .collect(Collectors.toSet()));

        BookDto bookDto = new BookDto(
                1L, book.getTitle(), book.getAuthor(), book.getIsbn(), book.getPrice(),
                book.getDescription(), book.getCoverImage(), book.getCategories()
                        .stream()
                        .map(Category::getId)
                        .collect(Collectors.toSet()));

        when(bookMapper.toEntity(createBookRequestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto savedBookDto = bookService.save(createBookRequestDto);

        assertThat(savedBookDto).isEqualTo(bookDto);
    }

    @Test
    @DisplayName("Find all books with pageable")
    public void findAll_ValidPageable_ReturnsBookDto() {
        Book book = new Book(1L, "First book", "First author", "978-0-123456-47-1",
                BigDecimal.valueOf(39.90), "first description", "img1.jpg",false,
                Set.of(new Category(1L), new Category(2L)));

        BookDto bookDto = new BookDto(book.getId(), book.getTitle(), book.getAuthor(),
                book.getIsbn(), book.getPrice(), book.getDescription(), book.getCoverImage(),
                book.getCategories()
                        .stream().map(Category::getId)
                        .collect(Collectors.toSet()));

        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(book);
        Page<Book> page = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAll(pageable)).thenReturn(page);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> bookDtos = bookService.findAll(pageable);

        assertThat(bookDtos).hasSize(1);
        assertThat(bookDtos.get(0)).isEqualTo(bookDto);
    }

    @Test
    @DisplayName("Find book by id with exist book")
    public void findById_ValidId_ReturnsBookDto() {
        Long bookId = 1L;

        Book book = new Book(bookId, "First book", "First author", "978-0-123456-47-1",
                BigDecimal.valueOf(39.90), "first description", "img1.jpg",false,
                Set.of(new Category(1L), new Category(2L)));

        BookDto bookDto = new BookDto(bookId, book.getTitle(), book.getAuthor(), book.getIsbn(),
                book.getPrice(), book.getDescription(), book.getCoverImage(),
                book.getCategories()
                .stream().map(Category::getId)
                .collect(Collectors.toSet()));

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto foundBook = bookService.findById(bookId);

        assertThat(foundBook).isEqualTo(bookDto);
    }

    @Test
    @DisplayName("Find book by id with non-existed id throw exception")
    public void findById_InvalidId_ThrowsException() {
        Long bookId = 100L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            bookService.findById(bookId);
        });
        String expected = "Can't find book with id: " + bookId;
        String actual = exception.getMessage();
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    @DisplayName("Delete book by id")
    public void deleteById_ValidId_DeletesBook() {
        Long bookId = 1L;

        bookService.deleteById(bookId);

        verify(bookRepository).deleteById(bookId);
    }

    @Test
    @DisplayName("Update book by id with exist book")
    public void updateById_ExistBook_ReturnsBookDto() {
        Long bookId = 1L;

        UpdateBookRequestDto updateBookRequestDto = new UpdateBookRequestDto("Update book",
                "Update author", "978-0-123456-47-1", BigDecimal.valueOf(39.90),
                "Update description", "img1.jpg", Set.of(1L, 2L));

        Book existingBook = new Book(bookId, "Existing title", "Existing author",
                "978-0-123456-47-0", BigDecimal.valueOf(19.90), "description",
                "img.jpg", false, updateBookRequestDto.getCategoriesIds()
                .stream().map(Category::new)
                .collect(Collectors.toSet()));

        BookDto bookDto = new BookDto(bookId, updateBookRequestDto.getTitle(),
                updateBookRequestDto.getAuthor(), updateBookRequestDto.getIsbn(),
                updateBookRequestDto.getPrice(), updateBookRequestDto.getDescription(),
                updateBookRequestDto.getCoverImage(), updateBookRequestDto.getCategoriesIds());

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(existingBook);
        when(bookMapper.toDto(existingBook)).thenReturn(bookDto);

        BookDto savedBookDto = bookService.updateById(bookId, updateBookRequestDto);

        assertThat(savedBookDto).isEqualTo(bookDto);
        verify(bookMapper).updateModelFromDto(updateBookRequestDto, existingBook);
        verify(bookRepository).save(existingBook);
    }
}
