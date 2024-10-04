package com.finpro.roomio_backends.image.entity.dto;


import com.finpro.roomio_backends.image.entity.ImageRoom;
import lombok.Data;

@Data
public class RoomImageResponseDto {

  private Long id;
  private String imageName;
  private String imageUrl;

  public RoomImageResponseDto(ImageRoom imageRoom) {
    this.id = imageRoom.getId();
    this.imageName = imageRoom.getImageName();
    this.imageUrl = imageRoom.getImageUrl();
  }

 public RoomImageResponseDto toDto(ImageRoom image) {
    return new RoomImageResponseDto(image);
 }
}
