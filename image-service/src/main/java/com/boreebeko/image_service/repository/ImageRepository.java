package com.boreebeko.image_service.repository;

import com.boreebeko.image_service.domain.image.ImageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, UUID> {

    Page<ImageEntity> findImagesByUserID(String userID, Pageable pageable);
}
