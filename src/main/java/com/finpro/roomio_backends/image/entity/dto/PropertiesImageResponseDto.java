package com.finpro.roomio_backends.image.entity.dto;


import com.finpro.roomio_backends.image.entity.ImageProperties;
import lombok.Data;

@Data
public class PropertiesImageResponseDto {

  private Long id;
  private String imageName;
  private String imageUrl;

  public PropertiesImageResponseDto(ImageProperties imageProperties) {
    this.id = imageProperties.getId();
    this.imageName = imageProperties.getImageName();
    this.imageUrl = imageProperties.getImageUrl();
  }

 public PropertiesImageResponseDto toDto(ImageProperties image) {
    return new PropertiesImageResponseDto(image);
 }
}
