package com.finpro.roomio_backends.properties.service;

import com.finpro.roomio_backends.properties.entity.dto.FacilitiesResponseDto;

import java.util.List;

public interface RoomFacilityService {
    List<FacilitiesResponseDto> addFacilitiesToRoom(Long roomId, List<Integer> facilityIds);
    List<FacilitiesResponseDto> getFacilitiesForRoom(Long roomId);
}
