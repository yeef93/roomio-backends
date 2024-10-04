package com.finpro.roomio_backends.facilities.service;

import com.finpro.roomio_backends.facilities.entity.Facilities;
import com.finpro.roomio_backends.facilities.entity.dto.FacilitiesResponseDto;
import com.finpro.roomio_backends.facilities.repository.FacilitiesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacilitiesService {

    private final FacilitiesRepository facilitiesRepository;

    public FacilitiesService(FacilitiesRepository facilitiesRepository) {
        this.facilitiesRepository = facilitiesRepository;
    }

    public List<FacilitiesResponseDto> getAllFacilities() {
        List<Facilities> facilitiesList = facilitiesRepository.findAll();
        return facilitiesList.stream()
                .map(facility -> new FacilitiesResponseDto(
                        facility.getId(),
                        facility.getName(),
                        facility.getIcon(),
                        facility.getFacilitiesType() != null ? facility.getFacilitiesType().getName() : null
                ))
                .collect(Collectors.toList());
    }
}
