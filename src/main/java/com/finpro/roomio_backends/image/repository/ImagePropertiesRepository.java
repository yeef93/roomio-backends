package com.finpro.roomio_backends.image.repository;

import com.finpro.roomio_backends.image.entity.ImageProperties;
import com.finpro.roomio_backends.properties.entity.Properties;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImagePropertiesRepository extends JpaRepository<ImageProperties, Long> {
    List<ImageProperties> findByProperties(Properties property);
}
