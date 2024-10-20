package com.finpro.roomio_backends.properties.service.impl;

import com.finpro.roomio_backends.bookings.entity.Bookings;
import com.finpro.roomio_backends.categories.entity.Categories;
import com.finpro.roomio_backends.categories.repository.CategoriesRepository;
import com.finpro.roomio_backends.categories.service.CategoriesService;
import com.finpro.roomio_backends.image.service.ImageService;
import com.finpro.roomio_backends.properties.entity.Properties;
import com.finpro.roomio_backends.properties.entity.Rooms;
import com.finpro.roomio_backends.properties.entity.dto.PropertiesRequestDto;
import com.finpro.roomio_backends.properties.repository.PropertiesRepository;
import com.finpro.roomio_backends.properties.repository.RoomsPeakRateRepository;
import com.finpro.roomio_backends.properties.repository.RoomsRepository;
import com.finpro.roomio_backends.properties.service.PropertiesService;
import com.finpro.roomio_backends.users.entity.Users;
import com.finpro.roomio_backends.users.repository.UsersRepository;
import com.finpro.roomio_backends.users.service.UsersService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PropertiesServiceImpl implements PropertiesService {

    private final PropertiesRepository propertiesRepository;
    private final RoomsRepository roomsRepository;
    private final UsersRepository usersRepository;
    private final ImageService imageService;
    private final UsersService usersService;
    private final CategoriesService categoriesService;
    private final CategoriesRepository categoriesRepository;
    private final RoomsPeakRateRepository roomsPeakRateRepository;

    public PropertiesServiceImpl(PropertiesRepository propertiesRepository, UsersRepository usersRepository,
                                 ImageService imageService, UsersService usersService, CategoriesService categoriesService,
                                 CategoriesRepository categoriesRepository, RoomsRepository roomsRepository,
                                 RoomsPeakRateRepository roomsPeakRateRepository) {
        this.propertiesRepository = propertiesRepository;
        this.usersRepository = usersRepository;
        this.imageService = imageService;
        this.usersService = usersService;
        this.categoriesService = categoriesService;
        this.categoriesRepository = categoriesRepository;
        this.roomsRepository = roomsRepository;
        this.roomsPeakRateRepository = roomsPeakRateRepository;
    }


    @Transactional
    @Override
    public Properties createProperty(PropertiesRequestDto requestDto) {
        Users tenant = usersService.getCurrentUser();

        // Check if the current user is allowed to create a property
        if (!tenant.getIsTenant()) {
            throw new AccessDeniedException("You do not have permission to create properties.");
        }

        // Check if the property name already exists
        Optional<Properties> existingProperty = getPropertyByName(requestDto.getName());
        if (existingProperty.isPresent()) {
            throw new IllegalArgumentException("Property with name '" + requestDto.getName() + "' already exists.");
        }

        // Fetch category by id
        Categories category = categoriesRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + requestDto.getCategoryId()));

        // Create a new property
        Properties property = new Properties();
        property.setName(requestDto.getName());
        property.setDescription(requestDto.getDescription());
        property.setLocation(requestDto.getLocation());
        property.setCity(requestDto.getCity());
        property.setMap(requestDto.getMap());
        property.setCategories(category);  // Set the category to the property
        property.setUser(tenant);  // Associate property with the current user

        return propertiesRepository.save(property);
    }


    // Get all property
    @Override
    public List<Properties> getAllProperties() {
        return propertiesRepository.findAll();
    }


//    @Override
//    public Page<Properties> getProperties(String search, String city, String sortBy, String direction, int page, int size) {
//        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
//        Pageable pageable = PageRequest.of(page, size, sort);
//
//        // If both search and city are provided, filter by both
//        if (search != null && !search.isEmpty() && city != null && !city.isEmpty()) {
//            return propertiesRepository.findByNameContainingIgnoreCaseAndCityIgnoreCase(search, city, pageable);
//        }
//        // If only search is provided, filter by search
//        else if (search != null && !search.isEmpty()) {
//            return propertiesRepository.findByNameContainingIgnoreCase(search, pageable);
//        }
//        // If only city is provided, filter by city
//        else if (city != null && !city.isEmpty()) {
//            return propertiesRepository.findByCityIgnoreCase(city, pageable);
//        }
//        // If neither search nor city are provided, return all properties
//        else {
//            return propertiesRepository.findAll(pageable);
//        }
//    }


    @Override
    public Page<Properties> getProperties(String search, String city, Users tenant,
                                          Integer minCapacity, BigDecimal minPrice, BigDecimal maxPrice,
                                          Long categoryId, LocalDate checkIn, LocalDate checkOut,
                                          String sortBy, String direction, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return propertiesRepository.findAll((root, query, cb) -> {
            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            if (search != null && !search.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%"));
            }
            if (city != null && !city.isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("city")), city.toLowerCase()));
            }
            if (tenant != null) {
                predicates.add(cb.equal(root.get("user"), tenant));
            }
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("categories").get("id"), categoryId));
            }

            if (minCapacity != null || minPrice != null || maxPrice != null || (checkIn != null && checkOut != null)) {
                Subquery<Long> roomSubquery = query.subquery(Long.class);
                Root<Rooms> roomRoot = roomSubquery.from(Rooms.class);

                List<Predicate> roomPredicates = new ArrayList<>();
                roomPredicates.add(cb.equal(roomRoot.get("properties"), root));
                roomPredicates.add(cb.isTrue(roomRoot.get("isActive")));

                if (minCapacity != null) {
                    roomPredicates.add(cb.greaterThanOrEqualTo(roomRoot.get("capacity"), minCapacity));
                }
                if (minPrice != null) {
                    roomPredicates.add(cb.greaterThanOrEqualTo(roomRoot.get("actualPrice"), minPrice));
                }
                if (maxPrice != null) {
                    roomPredicates.add(cb.lessThanOrEqualTo(roomRoot.get("actualPrice"), maxPrice));
                }

                if (checkIn != null && checkOut != null) {
                    Subquery<Long> bookingSubquery = query.subquery(Long.class);
                    Root<Bookings> bookingRoot = bookingSubquery.from(Bookings.class);
                    bookingSubquery.select(cb.count(bookingRoot))
                            .where(cb.equal(bookingRoot.get("room"), roomRoot),
                                    cb.lessThan(bookingRoot.get("checkInDate"), checkOut),
                                    cb.greaterThan(bookingRoot.get("checkOutDate"), checkIn));
                    roomPredicates.add(cb.equal(bookingSubquery, 0L));
                }

                roomSubquery.select(roomRoot.get("id"))
                        .where(cb.and(roomPredicates.toArray(new Predicate[0])));

                predicates.add(cb.exists(roomSubquery));
            }

            predicates.add(cb.isTrue(root.get("isActive")));

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }



    // Get a property by ID
    @Override
    public  Optional<Properties> getPropertyById(Long id) {
        return propertiesRepository.findById(id);
    }

    // Get a property by name
    @Override
    public Optional<Properties> getPropertyByName(String name) {
        return propertiesRepository.findByName(name);
    }

    @Override
    public Optional<Properties> updateProperty(Long id, PropertiesRequestDto requestDto) {
        // Fetch the existing property by id
        Optional<Properties> existingProperty = propertiesRepository.findById(id);

        // Get the current tenant (user)
        Users tenant = usersService.getCurrentUser();

        // Check if the current user is allowed to update the property
        if (!tenant.getIsTenant()) {
            throw new AccessDeniedException("You do not have permission to update properties.");
        }

        if (existingProperty.isPresent()) {
            Properties property = existingProperty.get();

            // Check if the new property name is already in use by another property
            Optional<Properties> propertyByName = propertiesRepository.findByName(requestDto.getName());
            if (propertyByName.isPresent() && !propertyByName.get().getId().equals(property.getId())) {
                throw new IllegalArgumentException("Property with name '" + requestDto.getName() + "' already exists.");
            }

            // Check category by id
            Categories category = categoriesRepository.findById(requestDto.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + requestDto.getCategoryId()));

            // Proceed with the update
            property.setName(requestDto.getName());
            property.setDescription(requestDto.getDescription());
            property.setLocation(requestDto.getLocation());
            property.setCity(requestDto.getCity());
            property.setMap(requestDto.getMap());
            property.setCategories(category);  // Set the category to the property
            property.setUser(tenant);  // Associate property with the current user

            // Save the updated property
            return Optional.of(propertiesRepository.save(property));
        } else {
            return Optional.empty(); // Return empty if the property does not exist
        }
    }


    @Override
    public boolean deleteProperty(Long id) {
        Optional<Properties> property = propertiesRepository.findById(id);
        if (property.isPresent()) {
            propertiesRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public void changeStatusProperty(Long propertyId) {
        Properties property = propertiesRepository.findById(propertyId)
                .orElseThrow(() -> new EntityNotFoundException("Property not found"));

        if (property.getIsActive()) {
            property.setIsActive(false); // Deactivate
        } else {
            property.setIsActive(true);  // Activate
        }
        propertiesRepository.save(property);
    }

    @Override
    public List<String> getDistinctCities() {
        return propertiesRepository.findDistinctCities();
    }

//
//    @Override
//    // Method to get all rooms with peak rates
//    public List<RoomResponseDto> getRoomsWithPeakRates(Long propertyId) {
//        List<Rooms> rooms = roomsRepository.findByPropertiesId(propertyId);
//
//        return rooms.stream().map(room -> {
//            BigDecimal peakRate = getPeakRateForRoom(room.getId());
//            return new RoomResponseDto(room, peakRate);
//        }).collect(Collectors.toList());
//    }
//
//    @Override
//    // Method to get peak rate for a specific room
//    public BigDecimal getPeakRateForRoom(Long roomId) {
//        return roomsPeakRateRepository.findCurrentRateForRoom(roomId, LocalDate.now())
//                .map(rate -> rate.getRateValue())
//                .orElse(null);
//    }
}
