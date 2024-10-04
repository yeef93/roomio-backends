package com.finpro.roomio_backends.image.entity.dto;

import com.finpro.roomio_backends.image.entity.ImageUserAvatar;
import lombok.Data;

@Data
public class AvatarImageResponseDto {
  private Long id;
  private String imageName;
  private String imageUrl;
  private String user;

  public AvatarImageResponseDto(ImageUserAvatar imageUserAvatar) {
    this.id = imageUserAvatar.getId();
    this.imageName = imageUserAvatar.getImageName();
    this.imageUrl = imageUserAvatar.getImageUrl();
    this.user = imageUserAvatar.getUser().getEmail();
  }

  public AvatarImageResponseDto toDto(ImageUserAvatar imageUserAvatar) {
    return new AvatarImageResponseDto(imageUserAvatar);
  }
}
