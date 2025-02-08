package com.boreebeko.image_service.web.controller;


import com.boreebeko.image_service.domain.exception.ExceptionBody;
import com.boreebeko.image_service.domain.exception.ImageUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ControllerAdvice {

    @ExceptionHandler(ImageUploadException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleImageUploadController(ImageUploadException exception) {
        return new ExceptionBody(exception.getMessage());
    }
}
