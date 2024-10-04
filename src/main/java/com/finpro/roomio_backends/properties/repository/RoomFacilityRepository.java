package com.finpro.roomio_backends.properties.repository;

import com.finpro.roomio_backends.properties.entity.RoomFacility;
import com.finpro.roomio_backends.properties.entity.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomFacilityRepository extends JpaRepository<RoomFacility, Long> {
    List<RoomFacility> findByRooms(Rooms room);
}
