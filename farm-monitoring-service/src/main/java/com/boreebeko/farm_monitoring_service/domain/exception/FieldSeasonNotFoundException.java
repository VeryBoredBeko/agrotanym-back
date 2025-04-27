package com.boreebeko.farm_monitoring_service.domain.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FieldSeasonNotFoundException extends RuntimeException {
    public FieldSeasonNotFoundException(String message) {
        super(message);
    }
}
