package com.finpro.roomio_backends.categories.service;

import com.finpro.roomio_backends.categories.entity.Categories;
import com.finpro.roomio_backends.categories.entity.dto.CategoriesRequestDto;
import com.finpro.roomio_backends.image.entity.ImageCategories;
import com.finpro.roomio_backends.image.entity.dto.ImageUploadRequestDto;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface CategoriesService {
    // uploading picture per category
    ImageCategories uploadImage(ImageUploadRequestDto requestDto);

    // create a new category
    Categories createCategory(CategoriesRequestDto category);

    // get all categories
    Page<Categories> getAllCategories(int page, int size, String sortBy, String direction);

    // get category by ID
    Optional<Categories> getCategoryById(Long id);

    // get category by name
    Optional<Categories> getCategoryByName(String name);

    //update category
    Optional<Categories> updateCategory(Long id, CategoriesRequestDto requestDto);

    //delete category
    boolean deleteCategory(Long id);

}
