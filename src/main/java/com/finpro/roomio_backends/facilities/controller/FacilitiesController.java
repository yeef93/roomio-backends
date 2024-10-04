package com.finpro.roomio_backends.facilities.controller;

import com.finpro.roomio_backends.facilities.entity.dto.FacilitiesResponseDto;
import com.finpro.roomio_backends.facilities.service.FacilitiesService;
import com.finpro.roomio_backends.responses.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v1/facility")
public class FacilitiesController {

    private final FacilitiesService facilitiesService;

    public FacilitiesController(FacilitiesService facilitiesService) {
        this.facilitiesService = facilitiesService;
    }

    @GetMapping
    public ResponseEntity<Response<List<FacilitiesResponseDto>>> getFacilities() {
        List<FacilitiesResponseDto> facilitiesList = facilitiesService.getAllFacilities();
        return Response.successfulResponse(HttpStatus.OK.value(), "Facilities retrieved successfully", facilitiesList);
    }
}
