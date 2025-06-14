package com.mate.service;

import com.mate.dto.BookDto;
import com.mate.dto.CreateBookRequestDto;
import com.mate.dto.UpdateBookRequestDto;
import java.util.List;

public interface BookService {

    BookDto save(CreateBookRequestDto createBookRequestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);

    void deleteById(Long id);

    BookDto updateById(Long id, UpdateBookRequestDto updateBookRequestDto);

}
