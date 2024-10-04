package com.finpro.roomio_backends.image.entity.dto;

import com.finpro.roomio_backends.image.entity.ImageProperties;
import lombok.Data;

@Data
public class ImagePropertiesListDto {
    private Long id;
    private String imageUrl;

    // Constructor that accepts an ImageProperties entity
    public ImagePropertiesListDto(ImageProperties imageProperties) {
        this.id = imageProperties.getId();
        this.imageUrl = imageProperties.getImageUrl();
    }
}
