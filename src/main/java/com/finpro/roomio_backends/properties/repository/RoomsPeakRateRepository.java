package com.finpro.roomio_backends.properties.repository;


import com.finpro.roomio_backends.properties.entity.RoomPeakRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoomsPeakRateRepository extends JpaRepository<RoomPeakRate, Long> {

//    @Query("SELECT r FROM RoomPeakRate r WHERE r.room.property.id = :propertyId AND r.deletedAt IS NULL")
//    List<RoomPeakRate> findActivePeakRatesForProperty(@Param("propertyId") Long propertyId);
}
