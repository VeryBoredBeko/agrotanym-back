package com.boreebeko.farm_monitoring_service.domain.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FieldNotFoundException extends RuntimeException {
    public FieldNotFoundException(String message) {
        super(message);
    }
}
