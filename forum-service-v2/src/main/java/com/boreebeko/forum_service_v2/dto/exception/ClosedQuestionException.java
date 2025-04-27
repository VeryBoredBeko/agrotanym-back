package com.boreebeko.forum_service_v2.dto.exception;

public class ClosedQuestionException extends RuntimeException {
    public ClosedQuestionException() {
    }
    public ClosedQuestionException(String message) {
        super(message);
    }
}
