package com.finpro.roomio_backends.properties.service.impl;

import com.finpro.roomio_backends.facilities.entity.Facilities;
import com.finpro.roomio_backends.properties.entity.RoomFacility;
import com.finpro.roomio_backends.properties.entity.Rooms;
import com.finpro.roomio_backends.properties.entity.dto.FacilitiesResponseDto;
import com.finpro.roomio_backends.facilities.repository.FacilitiesRepository;
import com.finpro.roomio_backends.properties.repository.RoomFacilityRepository;
import com.finpro.roomio_backends.properties.repository.RoomsRepository;
import com.finpro.roomio_backends.properties.service.RoomFacilityService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomFacilityServiceImpl  implements RoomFacilityService {

    private final RoomsRepository roomRepository;
    private final FacilitiesRepository facilityRepository;
    private final RoomFacilityRepository roomFacilityRepository;

    public RoomFacilityServiceImpl(RoomsRepository roomRepository,
                                   FacilitiesRepository facilityRepository,
                                   RoomFacilityRepository roomFacilityRepository) {
        this.roomRepository = roomRepository;
        this.facilityRepository = facilityRepository;
        this.roomFacilityRepository = roomFacilityRepository;
    }

    @Override
    @Transactional
    public List<FacilitiesResponseDto> addFacilitiesToRoom(Long roomId, List<Integer> facilityIds) {
        // Fetch the room by its ID
        Rooms room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID: " + roomId));

        // List to store added facilities
        List<FacilitiesResponseDto> addedFacilities = new ArrayList<>();

        // Iterate over the list of facility IDs and associate each facility with the room
        for (Integer facilityId : facilityIds) {
            Facilities facility = facilityRepository.findById(Long.valueOf(facilityId))
                    .orElseThrow(() -> new IllegalArgumentException("Invalid facility ID: " + facilityId));

            // Create a new RoomFacility entry
            RoomFacility roomFacility = new RoomFacility();
            roomFacility.setRooms(room);
            roomFacility.setFacilities(facility);

            // Save the association to the database
            roomFacilityRepository.save(roomFacility);

            // Add the facility to the response list
            addedFacilities.add(new FacilitiesResponseDto(facility.getId(), facility.getName(), facility.getIcon()));
        }

        // Return the list of added facilities
        return addedFacilities;
    }

    @Override
    @Transactional
    public List<FacilitiesResponseDto> getFacilitiesForRoom(Long roomId) {
        // Fetch the room to ensure it exists
        Rooms room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID: " + roomId));

        // Get all RoomFacility entries for this room
        List<RoomFacility> roomFacilities = roomFacilityRepository.findByRooms(room);

        // Map the RoomFacility entries to FacilityResponseDto
        return roomFacilities.stream()
                .map(rf -> new FacilitiesResponseDto(
                        rf.getFacilities().getId(),
                        rf.getFacilities().getName(),
                        rf.getFacilities().getIcon()))
                .collect(Collectors.toList());
    }
}
