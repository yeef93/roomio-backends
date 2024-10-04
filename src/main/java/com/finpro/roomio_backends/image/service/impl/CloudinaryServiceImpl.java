package com.finpro.roomio_backends.image.service.impl;

import com.cloudinary.Cloudinary;
import com.finpro.roomio_backends.image.service.CloudinaryService;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Data
public class CloudinaryServiceImpl implements CloudinaryService {

  @Resource
  private Cloudinary cloudinary;

  @Override
  public String uploadFile(MultipartFile file, String folderName) {
    try{
      // Configure upload option
      HashMap<Object, Object> options = new HashMap<>();
      options.put("folder", folderName);

      // Perform the upload
      Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);

      // Extract publicId and generate URL
      String publicId = (String) uploadedFile.get("public_id");
      return cloudinary.url().secure(true).generate(publicId);

    }catch (IOException e){
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

}
