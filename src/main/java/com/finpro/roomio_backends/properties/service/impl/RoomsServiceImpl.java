package com.finpro.roomio_backends.properties.service.impl;

import com.finpro.roomio_backends.exceptions.ObjectNotFoundException;
import com.finpro.roomio_backends.properties.entity.Properties;
import com.finpro.roomio_backends.properties.entity.Rooms;
import com.finpro.roomio_backends.properties.entity.dto.RoomRequestDto;
import com.finpro.roomio_backends.properties.entity.dto.RoomResponseDto;
import com.finpro.roomio_backends.properties.repository.PropertiesRepository;
import com.finpro.roomio_backends.properties.repository.RoomsRepository;
import com.finpro.roomio_backends.properties.service.RoomsService;
import com.finpro.roomio_backends.users.entity.Users;
import com.finpro.roomio_backends.users.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomsServiceImpl implements RoomsService {

    private final RoomsRepository roomsRepository;
    private final PropertiesRepository propertiesRepository;
    private final UsersService usersService;


    public RoomsServiceImpl(RoomsRepository roomRepository, PropertiesRepository propertiesRepository,
                            UsersService usersService) {
        this.roomsRepository = roomRepository;
        this.propertiesRepository = propertiesRepository;
        this.usersService = usersService;
    }

    public RoomResponseDto addRoom(Long propertyId, RoomRequestDto roomDTO) {

        Users tenant = usersService.getCurrentUser();
        // Check if the current user is allowed to create a category
        if (!tenant.getIsTenant()) {
            throw new AccessDeniedException("You do not have permission to create room.");
        }

        // Fetch the property by its ID
        Properties property = propertiesRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid property ID: " + propertyId));

        Rooms room = new Rooms();
        room.setProperties(property);
        room.setName(roomDTO.getName());
        room.setDescription(roomDTO.getDescription());
        room.setCapacity(roomDTO.getCapacity());
        room.setSize(roomDTO.getSize());
        room.setBedType(roomDTO.getBedType());
        room.setTotalBed(roomDTO.getTotalBed());
        room.setQty(roomDTO.getQty());
        room.setBasePrice(roomDTO.getBasePrice());
        room.setTotalBathroom(roomDTO.getTotalBathroom());
        room.setIsActive(true);

        Rooms savedRoom = roomsRepository.save(room);
        return new RoomResponseDto(savedRoom);
    }

    @Override
    public List<RoomResponseDto> getRoomsByPropertyId(Long propertyId) {
        List<Rooms> rooms = roomsRepository.findByPropertiesId(propertyId);
        return rooms.stream()
                .map(RoomResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<RoomResponseDto> getRoomById(Long propertyId, Long roomId) {
        Optional<Rooms> roomOpt = roomsRepository.findByIdAndPropertiesId(roomId, propertyId);
        if (roomOpt.isPresent()) {
            RoomResponseDto roomResponse = new RoomResponseDto(roomOpt.get());
            return ResponseEntity.ok(roomResponse); // HTTP 200 OK with room details
        } else {
            // HTTP 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Override
    public RoomResponseDto updateRoom(Long propertyId, Long roomId, RoomRequestDto roomRequestDto) {
        Users tenant = usersService.getCurrentUser();

        // Check if the current user is allowed to create a room
        if (!tenant.getIsTenant()) {
            throw new AccessDeniedException("You do not have permission to update this room.");
        }

        // Find the room by roomId and propertyId
        Optional<Rooms> roomOpt = roomsRepository.findByIdAndPropertiesId(roomId, propertyId);
        if (roomOpt.isEmpty()) {
            throw new ObjectNotFoundException("Room with ID " + roomId + " not found for property with ID " + propertyId);
        }

        // Check if the room name already exists for the same property (excluding the current room)
        boolean isDuplicateName = roomsRepository.existsByNameAndPropertiesIdAndIdNot(roomRequestDto.getName(), propertyId, roomId);
        if (isDuplicateName) {
            throw new IllegalArgumentException("Room with name '" + roomRequestDto.getName() + "' already exists for property with ID " + propertyId);
        }

        // Update room details
        Rooms room = roomOpt.get();
        room.setName(roomRequestDto.getName());
        room.setDescription(roomRequestDto.getDescription());
        room.setCapacity(roomRequestDto.getCapacity());
        room.setSize(roomRequestDto.getSize());
        room.setBedType(roomRequestDto.getBedType());
        room.setTotalBed(roomRequestDto.getTotalBed());
        room.setTotalBathroom(roomRequestDto.getTotalBathroom());
        room.setQty(roomRequestDto.getQty());
        room.setBasePrice(roomRequestDto.getBasePrice());
        room.setIsActive(roomRequestDto.getIsActive());

        // Save updated room details
        Rooms updatedRoom = roomsRepository.save(room);

        // Return response DTO
        return new RoomResponseDto(updatedRoom);
    }


    @Override
    public RoomResponseDto deactivateRoom(Long propertyId, Long roomId) {
        Users tenant = usersService.getCurrentUser();
        // Check if the current user is allowed to create a category
        if (!tenant.getIsTenant()) {
            throw new AccessDeniedException("You do not have permission to create room.");
        }
        Optional<Rooms> roomOpt = roomsRepository.findByIdAndPropertiesId(roomId, propertyId);
        if (roomOpt.isEmpty()) {
            throw new ObjectNotFoundException("Room with ID " + roomId + " not found for property with ID " + propertyId);
        }

        Rooms room = roomOpt.get();
        room.setIsActive(false);  // Set the room as inactive
        room.setDeletedAt(Instant.now());  // Set the deleted timestamp
        Rooms updatedRoom = roomsRepository.save(room);  // Save changes

        return new RoomResponseDto(updatedRoom);  // Return updated room details
    }


}
