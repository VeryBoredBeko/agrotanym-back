package com.boreebeko.image_service.repository;

import com.boreebeko.image_service.domain.image.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, UUID> {

    List<ImageEntity> findImagesByUserID(String userID);
}
