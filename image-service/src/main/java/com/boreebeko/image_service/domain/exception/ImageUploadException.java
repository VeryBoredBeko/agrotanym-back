package com.boreebeko.image_service.domain.exception;

public class ImageUploadException extends RuntimeException {

    public ImageUploadException() {
    }

    public ImageUploadException(String message) {
        super(message);
    }
}
