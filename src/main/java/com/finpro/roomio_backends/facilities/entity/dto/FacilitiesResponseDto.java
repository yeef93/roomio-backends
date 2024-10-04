package com.finpro.roomio_backends.facilities.entity.dto;

import lombok.Data;

@Data
public class FacilitiesResponseDto {

    private Long id;
    private String name;
    private String icon;
    private String facilitiesTypeName;

    public FacilitiesResponseDto(Long id, String name, String icon, String facilitiesTypeName) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.facilitiesTypeName = facilitiesTypeName;
    }

}
