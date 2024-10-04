package com.finpro.roomio_backends.properties.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PropertiesRequestDto {
    @NotBlank(message = "Property name is required")
    private String name;

    @NotBlank(message = "Property description is required")
    private String description;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotBlank(message = "Property location is required")
    private String location;

    @NotBlank(message = "Property city is required")
    private String city;

    private String map;

}
