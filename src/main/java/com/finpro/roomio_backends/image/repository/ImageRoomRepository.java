package com.finpro.roomio_backends.image.repository;

import com.finpro.roomio_backends.image.entity.ImageRoom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ImageRoomRepository extends JpaRepository<ImageRoom, Long> {
}
