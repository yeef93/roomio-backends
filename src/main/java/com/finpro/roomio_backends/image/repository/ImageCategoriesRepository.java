package com.finpro.roomio_backends.image.repository;

import com.finpro.roomio_backends.image.entity.ImageCategories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageCategoriesRepository extends JpaRepository<ImageCategories, Long> {
}
