package com.mate.dto.category;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryDto {
    @NotNull
    private Long id;
    @NotNull
    private String name;
    private String description;
}
