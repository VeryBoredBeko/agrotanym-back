package com.boreebeko.image_service.web.controller;

import com.boreebeko.image_service.service.ImageStorageService;
import com.boreebeko.image_service.web.dto.ImageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v2/images")
@Tag(name = "Image processing", description = "API for uploading and processing images")
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

    @Operation(
            summary = "Upload an image",
            description = "Uploads an image file for processing",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Image uploaded successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid image file", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<Void> uploadImage(
            @Parameter(
                    description = "The image file to upload",
                    required = true,
                    content = @Content(mediaType = "application/octet-stream", schema = @Schema(type = "string", format = "binary"))
            )
            @RequestParam MultipartFile image
    ) {
        imageStorageService.upload(image);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteImage(@RequestParam String imageId) {
        imageStorageService.delete(UUID.fromString(imageId));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
