package com.finpro.roomio_backends.properties.entity.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomRequestDto {

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotBlank(message = "Description must not be blank")
    private String description;

    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;

    @Min(value = 1, message = "Size must be at least 1")
    private Integer size;

    @NotBlank(message = "Bed type must not be blank")
    private String bedType;

    @Min(value = 1, message = "Total bed must be at least 1")
    private Integer totalBed;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer qty;

    @NotNull(message = "Base price must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Base price must be greater than 0")
    private BigDecimal basePrice;

    @Min(value = 1, message = "Total bathroom must be at least 1")
    private Integer totalBathroom;

    private Boolean isActive;
}
