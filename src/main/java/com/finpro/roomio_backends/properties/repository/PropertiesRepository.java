package com.finpro.roomio_backends.properties.repository;

import com.finpro.roomio_backends.properties.entity.Properties;
import com.finpro.roomio_backends.users.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertiesRepository extends JpaRepository<Properties, Long>, JpaSpecificationExecutor<Properties> {
    Optional<Properties> findByName(String name);

    // Search by name
    Page<Properties> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Search by city
    Page<Properties> findByCityIgnoreCase(String city, Pageable pageable);

    // Search by both name and city
    Page<Properties> findByNameContainingIgnoreCaseAndCityIgnoreCase(String name, String city, Pageable pageable);

    // Use the user field instead of tenantId
    Page<Properties> findByNameContainingIgnoreCaseAndCityIgnoreCaseAndUser(String name, String city, Users user, Pageable pageable);

    Page<Properties> findByNameContainingIgnoreCaseAndUser(String name, Users user, Pageable pageable);

    Page<Properties> findByCityIgnoreCaseAndUser(String city, Users user, Pageable pageable);

    Page<Properties> findByUser(Users user, Pageable pageable);

    @Query("SELECT DISTINCT p.city FROM Properties p order by p.city asc ")
    List<String> findDistinctCities();

}

