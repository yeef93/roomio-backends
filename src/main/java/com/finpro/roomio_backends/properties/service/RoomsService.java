package com.finpro.roomio_backends.properties.service;

import com.finpro.roomio_backends.properties.entity.dto.RoomRequestDto;
import com.finpro.roomio_backends.properties.entity.dto.RoomResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RoomsService {

    RoomResponseDto addRoom(Long propertyId, RoomRequestDto roomDTO);
    List<RoomResponseDto> getRoomsByPropertyId(Long propertyId);
    ResponseEntity<RoomResponseDto> getRoomById(Long propertyId, Long roomId);
    RoomResponseDto updateRoom(Long propertyId, Long roomId, RoomRequestDto roomRequestDto);
    RoomResponseDto deactivateRoom(Long propertyId, Long roomId);
}
