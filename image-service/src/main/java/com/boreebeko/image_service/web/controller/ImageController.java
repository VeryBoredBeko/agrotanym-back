package com.boreebeko.image_service.web.controller;

import com.boreebeko.image_service.service.ImageStorageService;
import com.boreebeko.image_service.web.dto.ImageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "Image processing controller", description = "API for uploading, fetching and processing images")
public class ImageController {

    private final ImageStorageService imageStorageService;

    @Autowired
    public ImageController(ImageStorageService imageStorageService) {
        this.imageStorageService = imageStorageService;
    }

    @Operation(
            summary = "Get user uploaded images DTO",
            description = "Returns list of ImageDTO objects which was uploaded by user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched all user uploaded images successfully")
    })
    @GetMapping
    public ResponseEntity<List<ImageDTO>> getAllImages(@RequestParam(required = false, defaultValue = "0") int page) {
        return new ResponseEntity<>(imageStorageService.listImages(page), HttpStatus.OK);
    }

    @Operation(
            summary = "Upload image for processing",
            description = "Uploading image for processing. Function will wait 'image' named multipart-file to store it."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Image wasn't uploaded")
    })
    @PostMapping
    public ResponseEntity<ImageDTO> uploadImage(
            @Parameter(description = "User uploaded image multipart-file")
            @RequestParam MultipartFile image
    ) {
        return new ResponseEntity<>(imageStorageService.upload(image), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete uploaded image by image ID",
            description = "Function will wait for 'imageId' parameter and then delete it if exists."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Image wasn't deleted"),
            @ApiResponse(responseCode = "404", description = "There is no image with such ID")
    })
    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(
            @Parameter(description = "Request parameter imageId means ID of image which user wants to delete")
            @PathVariable String imageId
    ) {
        imageStorageService.delete(UUID.fromString(imageId));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
