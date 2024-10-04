package com.finpro.roomio_backends.properties.repository;

import com.finpro.roomio_backends.properties.entity.Properties;
import com.finpro.roomio_backends.properties.entity.PropertyFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyFacilityRepository extends JpaRepository<PropertyFacility, Long> {
    // Method to find all facilities associated with a given property
    List<PropertyFacility> findByProperties(Properties properties);

}
