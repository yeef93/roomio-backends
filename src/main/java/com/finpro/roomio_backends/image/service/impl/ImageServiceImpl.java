package com.finpro.roomio_backends.image.service.impl;


import com.finpro.roomio_backends.exceptions.image.ImageNotFoundException;
import com.finpro.roomio_backends.image.entity.ImageCategories;
import com.finpro.roomio_backends.image.entity.ImageProperties;
import com.finpro.roomio_backends.image.entity.ImageRoom;
import com.finpro.roomio_backends.image.entity.ImageUserAvatar;
import com.finpro.roomio_backends.image.entity.dto.ImageUploadRequestDto;
import com.finpro.roomio_backends.image.entity.dto.PropertiesImageResponseDto;
import com.finpro.roomio_backends.image.repository.ImageCategoriesRepository;
import com.finpro.roomio_backends.image.repository.ImagePropertiesRepository;
import com.finpro.roomio_backends.image.repository.ImageRoomRepository;
import com.finpro.roomio_backends.image.repository.ImageUserAvatarRepository;
import com.finpro.roomio_backends.image.service.CloudinaryService;
import com.finpro.roomio_backends.image.service.ImageService;
import com.finpro.roomio_backends.properties.entity.Properties;
import com.finpro.roomio_backends.properties.entity.Rooms;
import com.finpro.roomio_backends.properties.repository.PropertiesRepository;
import com.finpro.roomio_backends.properties.repository.RoomsRepository;
import com.finpro.roomio_backends.users.entity.Users;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {

  private final ImageUserAvatarRepository imageUserAvatarRepository;
  private final ImageCategoriesRepository imageCategoriesRepository;
  private final ImagePropertiesRepository imagePropertiesRepository;
  private final ImageRoomRepository imageRoomRepository;
  private final CloudinaryService cloudinaryService;
  private final PropertiesRepository propertiesRepository;
  private final RoomsRepository roomsRepository;

  @Override
  public void saveAvatar(ImageUserAvatar imageUserAvatar) {
    imageUserAvatarRepository.save(imageUserAvatar);
  }


  @Override
  public ImageUserAvatar getAvatarById(Long imageId) {
    return imageUserAvatarRepository.findById(imageId)
        .orElseThrow(() -> new ImageNotFoundException("ImageUserAvatar not found"));
  }

  @Override
  @Transactional
  public ImageUserAvatar uploadAvatar(ImageUploadRequestDto requestDto, Users user) throws IllegalArgumentException {
    validateUploadRequest(requestDto);

    String imageName = requestDto.getFileName();
    String imageUrl = cloudinaryService.uploadFile(requestDto.getFile(),
        "roomio/users/" + user.getId().toString());

    ImageUserAvatar imageUserAvatar = new ImageUserAvatar();
    imageUserAvatar.setImageName(imageName);
    imageUserAvatar.setImageUrl(imageUrl);
    imageUserAvatar.setUser(user);
    if (imageUserAvatar.getImageUrl() == null) {
      // Handle error appropriately
      return null;
    }
    return imageUserAvatarRepository.save(imageUserAvatar);
  }


  private void validateUploadRequest(ImageUploadRequestDto requestDto) {
    if (requestDto.getFileName().isEmpty() || requestDto.getFile().isEmpty()) {
      throw new IllegalArgumentException("Invalid upload request");
    }
  }


  @Override
  @Transactional
  public ImageCategories uploadCategories(ImageUploadRequestDto requestDto, Users user) throws IllegalArgumentException {
    validateUploadRequest(requestDto);

    String imageName = requestDto.getFileName();
    String imageUrl = cloudinaryService.uploadFile(requestDto.getFile(),
            "roomio/categories/" + user.getId().toString());

    ImageCategories imageCategories = new ImageCategories();
    imageCategories.setImageName(imageName);
    imageCategories.setImageUrl(imageUrl);
    imageCategories.setUser(user);
    if (imageCategories.getImageUrl() == null) {
      // Handle error appropriately
      return null;
    }
    return imageCategoriesRepository.save(imageCategories);
  }

  @Override
  @Transactional
  public List<ImageProperties> uploadPropertyImage(Long propertyId, List<MultipartFile> files, Users user) throws IllegalArgumentException {


    // Fetch the property by its ID (handle exceptions as needed)
    Properties properties = propertiesRepository.findById(propertyId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid property ID: " + propertyId));

    // Initialize a list to store uploaded image properties
    List<ImageProperties> uploadedImages = new ArrayList<>();

    // Iterate through the list of files
    for (MultipartFile file : files) {
      try {
        // Get the original file name from the uploaded file
        String fileName = file.getOriginalFilename();

        // Upload the file to Cloudinary or any storage service you're using
        String imageUrl = cloudinaryService.uploadFile(file, "roomio/properties/" + propertyId);

        // Create a new ImageProperties object to save in the database
        ImageProperties imageProperties = new ImageProperties();
        imageProperties.setImageName(fileName);  // Set the original file name
        imageProperties.setImageUrl(imageUrl);   // Set the URL returned by Cloudinary
        imageProperties.setProperties(properties); // Associate the image with the property
        imageProperties.setUser(user);


        // Save the image information to the database
        imageProperties = imagePropertiesRepository.save(imageProperties);

        // Add the uploaded image to the list
        uploadedImages.add(imageProperties);
      } catch (Exception e) {
        // Handle exceptions such as file upload failures
        e.printStackTrace(); // Log the error (or handle it as needed)
      }
    }

    // Return the list of uploaded images
    return uploadedImages;
  }

  // Fetch images for a property by its ID
  @Override
  @Transactional
  public List<PropertiesImageResponseDto> getImagesByPropertyId(Long propertyId) {
    Properties property = propertiesRepository.findById(propertyId)
            .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + propertyId));

    List<ImageProperties> images = imagePropertiesRepository.findByProperties(property);

    return images.stream()
            .map(PropertiesImageResponseDto::new)
            .collect(Collectors.toList());
  }
  @Override
  public Optional<ImageCategories> findImageById(Long imageId) {
    return imageCategoriesRepository.findById(imageId);
  }

  @Override
  public ImageCategories findById(Long imageId) {
    return imageCategoriesRepository.findById(imageId)
            .orElseThrow(() -> new ImageNotFoundException("ImageUserAvatar not found"));
  }

  @Override
  @Transactional
  public List<ImageRoom> uploadRoomImage(Long roomId, List<MultipartFile> files, Users user) throws IllegalArgumentException {

    // Fetch the room by its ID (handle exceptions as needed)
    Rooms rooms = roomsRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid property ID: " + roomId));

    // Initialize a list to store uploaded image room
    List<ImageRoom> uploadedImages = new ArrayList<>();

    // Iterate through the list of files
    for (MultipartFile file : files) {
      try {
        // Get the original file name from the uploaded file
        String fileName = file.getOriginalFilename();

        // Upload the file to Cloudinary or any storage service you're using
        String imageUrl = cloudinaryService.uploadFile(file, "roomio/properties/" + roomId);

        // Create a new Imageroom object to save in the database
        ImageRoom imageRoom = new ImageRoom();
        imageRoom.setImageName(fileName);  // Set the original file name
        imageRoom.setImageUrl(imageUrl);   // Set the URL returned by Cloudinary
        imageRoom.setRooms(rooms); // Associate the image with the property
        imageRoom.setUser(user);


        // Save the image information to the database
        imageRoom = imageRoomRepository.save(imageRoom);

        // Add the uploaded image to the list
        uploadedImages.add(imageRoom);
      } catch (Exception e) {
        // Handle exceptions such as file upload failures
        e.printStackTrace(); // Log the error (or handle it as needed)
      }
    }

    // Return the list of uploaded images
    return uploadedImages;
  }



}
