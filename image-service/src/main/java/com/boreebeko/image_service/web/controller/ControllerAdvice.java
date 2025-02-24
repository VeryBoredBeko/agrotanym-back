package com.boreebeko.image_service.web.controller;

import com.boreebeko.image_service.domain.exception.ExceptionBody;
import com.boreebeko.image_service.domain.exception.ImageDeleteException;
import com.boreebeko.image_service.domain.exception.ImageNotFoundException;
import com.boreebeko.image_service.domain.exception.ImageUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ImageUploadException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleImageUploadController(ImageUploadException exception) {
        return new ExceptionBody(exception.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionBody handleAccessDeniedException(AccessDeniedException exception) {
        return new ExceptionBody(exception.getMessage());
    }

    @ExceptionHandler(ImageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionBody handleImageNotFoundException(ImageNotFoundException exception) {
        return new ExceptionBody(exception.getMessage());
    }

    @ExceptionHandler(ImageDeleteException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionBody handleImageDeleteException(ImageDeleteException exception) {
        return new ExceptionBody(exception.getMessage());
    }
}
