package com.boreebeko.farm_monitoring_service.domain.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
}
