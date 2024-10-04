package com.finpro.roomio_backends.image.entity.dto;


import com.finpro.roomio_backends.image.entity.ImageCategories;
import lombok.Data;

@Data
public class CategoryImageResponseDto {

  private Long id;
  private String imageName;
  private String imageUrl;

  public CategoryImageResponseDto(ImageCategories imageCategories) {
    this.id = imageCategories.getId();
    this.imageName = imageCategories.getImageName();
    this.imageUrl = imageCategories.getImageUrl();
  }

 public CategoryImageResponseDto toDto(ImageCategories image) {
    return new CategoryImageResponseDto(image);
 }
}
