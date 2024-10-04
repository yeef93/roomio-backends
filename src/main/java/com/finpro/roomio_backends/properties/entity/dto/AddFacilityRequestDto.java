package com.finpro.roomio_backends.properties.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class AddFacilityRequestDto {

    @NotBlank(message = "Property is required")
    private Long propertyId;
    private List<Integer> facilityIds;
}
