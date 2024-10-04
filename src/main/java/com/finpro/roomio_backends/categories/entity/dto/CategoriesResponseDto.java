package com.finpro.roomio_backends.categories.entity.dto;

import com.finpro.roomio_backends.categories.entity.Categories;
import lombok.Data;

import java.time.Instant;

@Data
public class CategoriesResponseDto {
    private Long id;
    private String name;
    private String description;
    private ImageDto image;
    private TenantDto tenant;

    @Data
    public static class ImageDto {
        private Long id;
        private String imageUrl;
    }

    // Constructor that accepts a Categories entity
    public CategoriesResponseDto(Categories categories) {
        this.id = categories.getId();
        this.name = categories.getName();
        this.description = categories.getDescription();

        // Check if image exists and map to ImageDto
        if (categories.getImageCategories() != null) {
            this.image = new ImageDto();
            this.image.setId(categories.getImageCategories().getId());
            this.image.setImageUrl(categories.getImageCategories().getImageUrl());
        }

        // Map tenant data including first name, last name, avatar, and created date
        this.tenant = new TenantDto(
                categories.getUser().getId(),
                categories.getUser().getEmail(),
                categories.getUser().getFirstname(),
                categories.getUser().getLastname(),
                categories.getUser().getAvatar().getImageUrl(),
                categories.getUser().getCreatedAt()
        );
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

    // Utility method to map Categories entity to CategoriesResponseDto
    public CategoriesResponseDto toDto(Categories categories) {
        return new CategoriesResponseDto(categories);
    }
}