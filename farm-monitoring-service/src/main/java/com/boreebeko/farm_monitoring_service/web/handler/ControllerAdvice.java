package com.boreebeko.farm_monitoring_service.web.handler;

import com.boreebeko.farm_monitoring_service.domain.exception.AccessDeniedException;
import com.boreebeko.farm_monitoring_service.domain.exception.ExceptionBody;
import com.boreebeko.farm_monitoring_service.domain.exception.FieldNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionBody handleAccessDeniedException(AccessDeniedException exception) {
        return new ExceptionBody(exception.getMessage());
    }

    @ExceptionHandler(FieldNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleFieldNotFoundException(FieldNotFoundException exception) {
        return new ExceptionBody(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return new ExceptionBody(exception.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleIllegalStateException(IllegalStateException exception) {
        return new ExceptionBody(exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleIllegalArgumentException(IllegalArgumentException exception) {
        return new ExceptionBody(exception.getMessage());
    }
}
