package com.boreebeko.image_service.web.controller;

import com.boreebeko.image_service.domain.image.Image;
import com.boreebeko.image_service.service.ImageUploadService;
import com.boreebeko.image_service.service.KafkaProducerService;
import com.boreebeko.image_service.service.event.ImageUploadedEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImageUploadController {

    private final ImageUploadService imageUploadService;
    private final KafkaProducerService kafkaProducerService;

    public ImageUploadController(ImageUploadService imageUploadService, KafkaProducerService kafkaProducerService) {
        this.imageUploadService = imageUploadService;
        this.kafkaProducerService = kafkaProducerService;
    }

    @GetMapping(value = "/image")
    public ResponseEntity<String> getImage() {
        return new ResponseEntity<>("Carniflower Image", HttpStatus.OK);
    }

    @GetMapping(value = "/upload-image")
    public ResponseEntity<String> getHistoryOfUploads(@AuthenticationPrincipal Jwt jwt) {
        System.out.println(jwt.getClaimAsString("preferred_username"));
        return new ResponseEntity<>(jwt.getClaimAsString("preferred_username"), HttpStatus.OK);
    }

    @PostMapping(value = "/upload-image")
    public ResponseEntity<String> uploadImage(@AuthenticationPrincipal Jwt jwt,
                                              @RequestParam("file") MultipartFile file) {

        String userID = jwt.getClaimAsString("sub");

        String fileName = imageUploadService.upload(new Image(file), userID);
        kafkaProducerService.upload(new ImageUploadedEvent(fileName));

        return new ResponseEntity<>("Uploaded", HttpStatus.CREATED);
    }
}
