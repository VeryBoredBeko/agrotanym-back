package com.boreebeko.image_service.web.controller;

import com.boreebeko.image_service.service.ImageStorageService;
import com.boreebeko.image_service.web.dto.ImageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v2/images")
public class ImageController {

    private final ImageStorageService imageStorageService;

    @Autowired
    public ImageController(ImageStorageService imageStorageService) {
        this.imageStorageService = imageStorageService;
    }

    @GetMapping
    public ResponseEntity<List<ImageDTO>> getAllImages() {
        return new ResponseEntity<>(imageStorageService.listImages(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> uploadImage(@RequestParam MultipartFile image) {
        imageStorageService.upload(image);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteImage(@RequestParam String imageId) {
        imageStorageService.delete(UUID.fromString(imageId));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
