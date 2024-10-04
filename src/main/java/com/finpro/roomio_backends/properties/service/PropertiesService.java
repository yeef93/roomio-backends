package com.finpro.roomio_backends.properties.service;

import com.finpro.roomio_backends.properties.entity.Properties;
import com.finpro.roomio_backends.properties.entity.dto.PropertiesRequestDto;
import com.finpro.roomio_backends.users.entity.Users;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface PropertiesService {

    // create a new property
    Properties createProperty(PropertiesRequestDto requestDto);

    // get all Property
    List<Properties> getAllProperties();

    // get Property by ID
    Optional<Properties> getPropertyById(Long id);

    // get Property by name
    Optional<Properties> getPropertyByName(String name);

    //update property
    Optional<Properties> updateProperty(Long id, PropertiesRequestDto requestDto);

    //delete property
    boolean deleteProperty(Long id);

    Page<Properties> getProperties(String search, String city, Users tenant, String sortBy, String direction, int page, int size);

//    Page<Properties> getProperties(String search, String city, String sortBy, String direction, int page, int size);

//    List<RoomResponseDto> getRoomsWithPeakRates(Long propertyId);
//
//    BigDecimal getPeakRateForRoom(Long roomId);
}
