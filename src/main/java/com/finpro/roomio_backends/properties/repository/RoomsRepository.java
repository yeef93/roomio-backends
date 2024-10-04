package com.finpro.roomio_backends.properties.repository;


import com.finpro.roomio_backends.properties.entity.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomsRepository extends JpaRepository<Rooms, Long> {
    List<Rooms> findByPropertiesId(Long propertyId);
    Optional<Rooms> findByIdAndPropertiesId(Long roomId, Long propertyId);
    boolean existsByNameAndPropertiesIdAndIdNot(String name, Long propertyId, Long id);

//    @Query("SELECT MIN(r.basePrice) FROM Room r WHERE r.property.id = :propertyId AND r.isActive = true")
//    BigDecimal findMinBasePriceByPropertyId(@Param("propertyId") Long propertyId);
}
