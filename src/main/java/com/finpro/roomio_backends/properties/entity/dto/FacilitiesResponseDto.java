package com.finpro.roomio_backends.properties.entity.dto;

import lombok.Data;

@Data
public class FacilitiesResponseDto {
    private Long id;
    private String name;
    private String icon;

    public FacilitiesResponseDto(Long id, String name, String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }
}
