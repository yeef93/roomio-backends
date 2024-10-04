package com.finpro.roomio_backends.properties.entity.dto;

import com.finpro.roomio_backends.image.entity.dto.ImagePropertiesListDto;
import com.finpro.roomio_backends.properties.entity.Properties;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PropertiesResponseDto {

    private Long id;
    private String name;
    private String description;
    private String location;
    private String city;
    private String map;
    private Boolean isPublish;
    private CategoryDto category;  // Updated to hold both category name and image URL
    private TenantDto tenant; // Updated to hold tenant information as an object
    private List<ImagePropertiesListDto> images; // List of image DTOs
    private List<RoomResponseDto> rooms;

    // Constructor that accepts a Properties entity
    public PropertiesResponseDto(Properties properties) {
        this.id = properties.getId();
        this.name = properties.getName();
        this.description = properties.getDescription();
        this.location = properties.getLocation();
        this.city = properties.getCity();
        this.map = properties.getMap();
        this.isPublish = properties.getIsPublish();

        // Map category data including name and image URL
        this.category = new CategoryDto(
                properties.getCategories().getName(),
                properties.getCategories().getImageCategories().getImageUrl() // Assuming getImageUrl() exists in Category entity
        );

        // Map tenant data including first name, last name, avatar, and created date
        this.tenant = new TenantDto(
                properties.getUser().getId(),
                properties.getUser().getEmail(),
                properties.getUser().getFirstname(),
                properties.getUser().getLastname(),
                properties.getUser().getAvatar().getImageUrl(),
                properties.getUser().getCreatedAt()
        );

        // Map the list of ImageProperties to a list of ImagePropertiesListDto
        this.images = properties.getImages() != null ?
                properties.getImages().stream()
                        .map(ImagePropertiesListDto::new)
                        .collect(Collectors.toList())
                : List.of(); // Return an empty list if images is null

        // Map the list of rooms to a list of RoomDto
        this.rooms = properties.getRooms() != null ?
                properties.getRooms().stream()
                        .map(RoomResponseDto::new)
                        .collect(Collectors.toList())
                : List.of(); // Return an empty list if rooms is null
    }

    // Utility method to map Properties entity to PropertiesResponseDto
    public PropertiesResponseDto toDto(Properties properties) {
        return new PropertiesResponseDto(properties);
    }

    // Nested class for Category data
    @Data
    public static class CategoryDto {
        private String name;
        private String imageUrl;

        public CategoryDto(String name, String imageUrl) {
            this.name = name;
            this.imageUrl = imageUrl;
        }
    }

    // Nested class for Tenant data
    @Data
    public static class TenantDto {
        private Long id;
        private String email;
        private String firstname;
        private String lastname;
        private String avatar;  // Avatar image URL or path
        private Instant createdAt;

        public TenantDto(Long id, String email, String firstname, String lastname, String avatar, Instant createdAt) {
            this.id = id;
            this.email = email;
            this.firstname = firstname;
            this.lastname = lastname;
            this.avatar = avatar;
            this.createdAt = createdAt;
        }
    }
}
