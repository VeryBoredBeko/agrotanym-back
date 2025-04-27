package com.boreebeko.image_service.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImageClassificationResponse {

    private String message;
    private Integer width;
    private Integer height;
    private String format;
    private String label;
}
