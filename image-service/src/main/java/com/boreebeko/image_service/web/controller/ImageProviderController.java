package com.boreebeko.image_service.web.controller;

import com.boreebeko.image_service.service.ImageProviderService;
import com.boreebeko.image_service.web.dto.ImageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ImageProviderController {

    private final ImageProviderService providerService;

    public ImageProviderController(ImageProviderService providerService) {
        this.providerService = providerService;
    }

    @GetMapping(value = "/images/{userId}")
    public ResponseEntity<List<ImageDTO>> getAllImages(@PathVariable("userId") String userId) {

        List<ImageDTO> imageDTOS = providerService.getAllImages(userId);
        return new ResponseEntity<>(imageDTOS, HttpStatus.OK);
    }
}
