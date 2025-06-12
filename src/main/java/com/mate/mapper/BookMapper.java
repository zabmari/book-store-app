package com.mate.mapper;

import com.mate.config.MapperConfig;
import com.mate.dto.BookDto;
import com.mate.dto.CreateBookRequestDto;
import com.mate.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto createBookRequestDto);

}
