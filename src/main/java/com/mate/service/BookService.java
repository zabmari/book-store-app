package com.mate.service;

import com.mate.dto.BookDto;
import com.mate.dto.BookSearchParameters;
import com.mate.dto.CreateBookRequestDto;
import com.mate.dto.UpdateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {

    BookDto save(CreateBookRequestDto createBookRequestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    void deleteById(Long id);

    BookDto updateById(Long id, UpdateBookRequestDto updateBookRequestDto);

    List<BookDto> search(BookSearchParameters bookSearchParameters, Pageable pageable);

}
