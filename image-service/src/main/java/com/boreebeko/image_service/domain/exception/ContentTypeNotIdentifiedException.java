package com.boreebeko.image_service.domain.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ContentTypeNotIdentifiedException extends RuntimeException {

    public ContentTypeNotIdentifiedException(String message) {
        super(message);
    }
}
