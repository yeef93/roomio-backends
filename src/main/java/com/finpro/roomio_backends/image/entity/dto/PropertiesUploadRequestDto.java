package com.finpro.roomio_backends.image.entity.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PropertiesUploadRequestDto {
    private Long propertyId;
    private List<MultipartFile> files;
}
