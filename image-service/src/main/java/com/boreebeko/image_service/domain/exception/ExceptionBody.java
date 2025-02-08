package com.boreebeko.image_service.domain.exception;

public class ExceptionBody {

    private String message;

    public ExceptionBody(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
