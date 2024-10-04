package com.finpro.roomio_backends.properties.service.impl;

import com.finpro.roomio_backends.facilities.entity.Facilities;
import com.finpro.roomio_backends.properties.entity.Properties;
import com.finpro.roomio_backends.properties.entity.PropertyFacility;
import com.finpro.roomio_backends.properties.entity.dto.FacilitiesResponseDto;
import com.finpro.roomio_backends.facilities.repository.FacilitiesRepository;
import com.finpro.roomio_backends.properties.repository.PropertiesRepository;
import com.finpro.roomio_backends.properties.repository.PropertyFacilityRepository;
import com.finpro.roomio_backends.properties.service.PropertyFacilityService;
import com.finpro.roomio_backends.users.entity.Users;
import com.finpro.roomio_backends.users.service.UsersService;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropertyFacilityServiceImpl implements PropertyFacilityService {

    private final PropertiesRepository propertiesRepository;
    private final FacilitiesRepository facilitiesRepository;
    private final PropertyFacilityRepository propertyFacilityRepository;
    private  final UsersService usersService;

    public PropertyFacilityServiceImpl(PropertiesRepository propertiesRepository,
                                 FacilitiesRepository facilitiesRepository,
                                       PropertyFacilityRepository propertyFacilityRepository,
                                       UsersService usersService) {
        this.propertiesRepository = propertiesRepository;
        this.facilitiesRepository = facilitiesRepository;
        this.propertyFacilityRepository = propertyFacilityRepository;
        this.usersService = usersService;
    }

    @Override
    @Transactional
    public List<FacilitiesResponseDto> addFacilitiesToProperty(Long propertyId, List<Integer> facilityIds) {
        Users tenant = usersService.getCurrentUser();

        // Check if the current user is allowed to create a category
        if (!tenant.getIsTenant()) {
            throw new AccessDeniedException("You do not have permission to create categories.");
        }
        // Fetch the property by its ID
        Properties property = propertiesRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid property ID: " + propertyId));

        // List to store added facilities
        List<FacilitiesResponseDto> addedFacilities = new ArrayList<>();

        // Iterate over the list of facility IDs and associate each facility with the property
        for (Integer facilityId : facilityIds) {
            Facilities facility = facilitiesRepository.findById(Long.valueOf(facilityId))
                    .orElseThrow(() -> new IllegalArgumentException("Invalid facility ID: " + facilityId));

            // Create a new PropertyFacility entry
            PropertyFacility propertyFacility = new PropertyFacility();
            propertyFacility.setProperties(property);
            propertyFacility.setFacilities(facility);

            // Save the association to the database
            propertyFacilityRepository.save(propertyFacility);

            // Add the facility to the response list
            addedFacilities.add(new FacilitiesResponseDto(facility.getId(), facility.getName(), facility.getIcon()));
        }

        // Return the list of added facilities
        return addedFacilities;
    }


    @Override
    @Transactional
    public List<FacilitiesResponseDto> getFacilitiesForProperty(Long propertyId) {
        // Fetch the property to ensure it exists
        Properties property = propertiesRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid property ID: " + propertyId));

        // Get all PropertyFacility entries for this property
        List<PropertyFacility> propertyFacilities = propertyFacilityRepository.findByProperties(property);

        // Map the PropertyFacility entries to FacilityResponseDto
        return propertyFacilities.stream()
                .map(pf -> new FacilitiesResponseDto(
                        pf.getFacilities().getId(),
                        pf.getFacilities().getName(),
                        pf.getFacilities().getIcon()))
                .collect(Collectors.toList());
    }
}
