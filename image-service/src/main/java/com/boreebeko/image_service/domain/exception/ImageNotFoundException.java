package com.boreebeko.image_service.domain.exception;

public class ImageNotFoundException extends RuntimeException {

    public ImageNotFoundException() {}
    public ImageNotFoundException(String message) {
        super(message);
    }
}
