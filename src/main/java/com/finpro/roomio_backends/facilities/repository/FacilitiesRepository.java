package com.finpro.roomio_backends.facilities.repository;

import com.finpro.roomio_backends.facilities.entity.Facilities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilitiesRepository extends JpaRepository<Facilities , Long> {
}
