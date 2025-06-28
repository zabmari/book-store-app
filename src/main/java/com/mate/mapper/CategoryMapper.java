package com.mate.mapper;

import com.mate.config.MapperConfig;
import com.mate.dto.category.CategoryDto;
import com.mate.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {

    CategoryDto toDto(Category category);

    Category toEntity(CategoryDto categoryDto);
}
