package com.boreebeko.image_service.web.controller;

import com.boreebeko.image_service.domain.image.Image;
import com.boreebeko.image_service.service.ImageUploadService;
import com.boreebeko.image_service.service.KafkaProducerService;
import com.boreebeko.image_service.service.event.ImageUploadedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

//@RestController
public class ImageUploadController {

//    private final ImageUploadService imageUploadService;
//    private final KafkaProducerService kafkaProducerService;
//
//    @Autowired
//    public ImageUploadController(ImageUploadService imageUploadService, KafkaProducerService kafkaProducerService) {
//        this.imageUploadService = imageUploadService;
//        this.kafkaProducerService = kafkaProducerService;
//    }
//
//    @PostMapping(value = "/images")
//    public ResponseEntity<String> uploadImage(@AuthenticationPrincipal Jwt jwt,
//                                              @RequestParam("file") MultipartFile file) {
//
//        // TODO: Secure uploading images
//
//        String userID = jwt.getClaimAsString("sub");
//
//        String fileName = imageUploadService.upload(new Image(file), userID);
//        kafkaProducerService.upload(new ImageUploadedEvent(fileName));
//
//        return new ResponseEntity<>("Uploaded", HttpStatus.CREATED);
//    }
}
