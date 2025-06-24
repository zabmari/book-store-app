package com.mate.mapper;

import com.mate.config.MapperConfig;
import com.mate.dto.book.BookDto;
import com.mate.dto.book.CreateBookRequestDto;
import com.mate.dto.book.UpdateBookRequestDto;
import com.mate.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto createBookRequestDto);

    void updateModelFromDto(UpdateBookRequestDto updateBookRequestDto, @MappingTarget Book book);

}
