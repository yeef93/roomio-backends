package com.finpro.roomio_backends.image.entity.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageUploadRequestDto {

  private String fileName;
  private MultipartFile file;

}
