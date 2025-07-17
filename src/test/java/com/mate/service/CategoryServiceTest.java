package com.mate.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mate.dto.category.CategoryDto;
import com.mate.exception.EntityNotFoundException;
import com.mate.mapper.CategoryMapper;
import com.mate.model.Category;
import com.mate.repository.category.CategoryRepository;
import java.util.List;
import java.util.Optional;
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
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Find all categories with pageable")
    public void findAll_ValidPageable_ReturnsCategoryDto() {
        Category category = new Category(1L,"first","first category",false);

        CategoryDto categoryDto = new CategoryDto(
                category.getId(), category.getName(), category.getDescription());

        Pageable pageable = PageRequest.of(0, 10);
        List<Category> categories = List.of(category);
        Page<Category> page = new PageImpl<>(categories, pageable, categories.size());

        when(categoryRepository.findAll(pageable)).thenReturn(page);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        List<CategoryDto> categoryDtos = categoryService.findAll(pageable);

        assertThat(categoryDtos).hasSize(1);
        assertThat(categoryDtos.get(0)).isEqualTo(categoryDto);
    }

    @Test
    @DisplayName("Get category by id with exist id")
    public void getById_ValidId_ReturnsCategoryDto() {
        Long categoryId = 1L;

        Category category = new Category(1L,"first","first category",false);

        CategoryDto categoryDto = new CategoryDto(
                categoryId, category.getName(), category.getDescription());

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto foundCategory = categoryService.getById(categoryId);

        assertThat(foundCategory).isEqualTo(categoryDto);
    }

    @Test
    @DisplayName("Get category by id with non-existed id throw exception")
    public void getById_InvalidId_ThrowsException() {
        Long categoryId = 100L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            categoryService.getById(categoryId);
        });
        String expected = "Can't find category with id: " + categoryId;
        String actual = exception.getMessage();
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    @DisplayName("Save new category")
    public void save_ValidCreateCategoryRequestDto_ReturnsCategoryDto() {

        CategoryDto createdDto = new CategoryDto(null, "First", "first category");

        Category category = new Category(
                null, createdDto.getName(), createdDto.getDescription(), false);

        CategoryDto expected = new CategoryDto(1L, category.getName(), category.getDescription());

        when(categoryMapper.toEntity(createdDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expected);

        CategoryDto savedCategoryDto = categoryService.save(createdDto);

        assertThat(savedCategoryDto).isEqualTo(expected);
    }

    @Test
    @DisplayName("Delete category by id")
    public void deleteById_ValidId_DeletesCategory() {
        Long categoryId = 1L;

        categoryService.deleteById(categoryId);

        verify(categoryRepository).deleteById(categoryId);
    }
}


