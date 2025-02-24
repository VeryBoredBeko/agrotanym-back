package com.boreebeko.image_service.service;

import com.boreebeko.image_service.web.dto.ImageDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Component
public interface ImageStorageService {

    void upload(MultipartFile image);
    void delete(UUID id);
    List<ImageDTO> listImages();
}
