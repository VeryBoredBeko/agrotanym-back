package com.boreebeko.image_service.service;

import com.boreebeko.image_service.web.dto.ImageDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * Interface for image storaging services
 * */
@Component
public interface ImageStorageService {

    /**
     * @param image image MultipartFile object uploaded by user
     * */
    ImageDTO upload(MultipartFile image);

    /**
     * @param id the id of user uploaded image
     * */
    void delete(UUID id);

    /**
     * @return list of ImageDTO, which were uploaded by the user
     * */
    List<ImageDTO> listImages(int page);
}
