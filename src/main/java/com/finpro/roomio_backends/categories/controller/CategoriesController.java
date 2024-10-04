package com.finpro.roomio_backends.categories.controller;

import com.finpro.roomio_backends.categories.entity.Categories;
import com.finpro.roomio_backends.categories.entity.dto.CategoriesRequestDto;
import com.finpro.roomio_backends.categories.entity.dto.CategoriesResponseDto;
import com.finpro.roomio_backends.categories.service.CategoriesService;
import com.finpro.roomio_backends.image.entity.ImageCategories;
import com.finpro.roomio_backends.image.entity.dto.CategoryImageResponseDto;
import com.finpro.roomio_backends.image.entity.dto.ImageUploadRequestDto;
import com.finpro.roomio_backends.responses.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;


@Slf4j
@RestController
@RequestMapping("/api/v1/categories")
public class CategoriesController {

    private final CategoriesService categoriesService;

    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    // * upload image
    @PostMapping("/image/upload")
    public ResponseEntity<Response<CategoryImageResponseDto>> uploadImage(@ModelAttribute ImageUploadRequestDto requestDto) {
        try {
            ImageCategories uploadedImageCategories = categoriesService.uploadImage(requestDto);
            if (uploadedImageCategories == null) {
                return ResponseEntity.noContent().build();
            } else {
                return Response.successfulResponse(HttpStatus.OK.value(), "Image successfully uploaded!", new CategoryImageResponseDto(uploadedImageCategories));
            }
        } catch (Exception e) {
            return Response.failedResponse(HttpStatus.BAD_REQUEST.value(), "Failed to get upload image: " + e.getMessage());
        }
    }

    // Create a new category
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoriesRequestDto requestDto) {
        try {
            // Create category using service
            Categories createdCategory = categoriesService.createCategory(requestDto);

            // Convert the created category to the response DTO
            CategoriesResponseDto responseDto = new CategoriesResponseDto(createdCategory);

            // Return success response with the DTO
            return Response.successfulResponse(
                    CREATED.value(),
                    "Category successfully created!",
                    responseDto);
        } catch (IllegalArgumentException e) {
            // Return a custom error response when category name already exists
            return Response.failedResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    e.getMessage(),
                    null
            );
        } catch (Exception e) {
            // Catch-all for other exceptions
            return Response.failedResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "An unexpected error occurred.",
                    null
            );
        }
    }

    // Get all categories
    @GetMapping
    public ResponseEntity<?> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        try {
            // Get paginated and sorted categories from the service
            Page<Categories> categoriesPage = categoriesService.getAllCategories(page, size, sortBy, direction);

            // Convert Page of Categories to List of CategoriesResponseDto
            List<CategoriesResponseDto> categoryDtos = categoriesPage.getContent()
                    .stream()
                    .map(CategoriesResponseDto::new)
                    .collect(Collectors.toList());

            // Create a response object with pagination details
            Map<String, Object> response = new HashMap<>();
            response.put("categories", categoryDtos);
            response.put("currentPage", categoriesPage.getNumber());
            response.put("totalItems", categoriesPage.getTotalElements());
            response.put("totalPages", categoriesPage.getTotalPages());
            response.put("pageSize", categoriesPage.getSize());

            // Return the response
            return Response.successfulResponse(OK.value(), "Categories retrieved successfully", response);
        } catch (Exception e) {
            return Response.failedResponse(HttpStatus.BAD_REQUEST.value(), "Failed to get categories: " + e.getMessage());
        }
    }



    // Get category by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        try {
            Optional<Categories> category = categoriesService.getCategoryById(id);
            // Map the entity to DTO and return the response
            return category.map(value -> {
                        CategoriesResponseDto categoryDto = new CategoriesResponseDto(value);
                        return Response.successfulResponse(HttpStatus.OK.value(),"Category found",categoryDto);
                    })
                    .orElseGet(() -> Response.failedResponse(HttpStatus.NOT_FOUND.value(),"Category not found"
                    ));
        } catch (Exception e) {
            return Response.failedResponse(HttpStatus.BAD_REQUEST.value(), "Failed to get category by ID: " + e.getMessage());
        }
    }

    // Update an existing category by ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id,@RequestBody CategoriesRequestDto requestDto) {
        try {
            Optional<Categories> updatedCategory = categoriesService.updateCategory(id, requestDto);
            // Check if category was found and updated
            if (updatedCategory.isPresent()) {
                CategoriesResponseDto responseDto = new CategoriesResponseDto(updatedCategory.get());
                return Response.successfulResponse(HttpStatus.OK.value(), "Category successfully updated!", responseDto);
            } else {
                return Response.failedResponse(HttpStatus.NOT_FOUND.value(), "Category not found", null);
            }
        } catch (AccessDeniedException e) {
            return Response.failedResponse(HttpStatus.FORBIDDEN.value(), e.getMessage(), null);
        } catch (IllegalArgumentException e) {
            return Response.failedResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        } catch (Exception e) {
            return Response.failedResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred.", null);
        }
    }

    // Delete a category by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            boolean isDeleted = categoriesService.deleteCategory(id);

            if (isDeleted) {
                return Response.successfulResponse(HttpStatus.OK.value(), "Category successfully deleted", null);
            } else {
                return Response.failedResponse(HttpStatus.NOT_FOUND.value(), "Category not found", null);
            }
        } catch (DataIntegrityViolationException e) {
            // Handle the case where the category has related entities
            return Response.failedResponse(HttpStatus.CONFLICT.value(), "Category cannot be deleted because it is linked to properties.", null);
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            return Response.failedResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred.", null);
        }
    }


}