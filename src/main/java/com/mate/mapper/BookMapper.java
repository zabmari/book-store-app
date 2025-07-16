package com.mate.mapper;

import com.mate.config.MapperConfig;
import com.mate.dto.book.BookDto;
import com.mate.dto.book.BookDtoWithoutCategoryIds;
import com.mate.dto.book.CreateBookRequestDto;
import com.mate.dto.book.UpdateBookRequestDto;
import com.mate.model.Book;
import com.mate.model.Category;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookDto toDto(Book book);

    @Mapping(target = "categories", source = "categoriesIds")
    Book toEntity(CreateBookRequestDto createBookRequestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    void updateModelFromDto(UpdateBookRequestDto updateBookRequestDto, @MappingTarget Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        if (book.getCategories() != null) {
            bookDto.setCategoriesIds(
                    book.getCategories()
                            .stream()
                            .map(Category::getId)
                            .collect(Collectors.toSet())
            );
        }
    }

    default Set<Category> mapCategoryIds(Set<Long> ids) {
        if (ids == null) {
            return null;
        }
        return ids.stream().map(Category::new).collect(Collectors.toSet());
    }
}
