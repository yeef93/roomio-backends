package com.finpro.roomio_backends.categories.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoriesRequestDto {
    @NotBlank(message = "Category name is required")
    private String name;

    @NotBlank(message = "Category description is required")
    private String description;

    @NotNull(message = "Image ID is required")
    private Long imageId;
}
