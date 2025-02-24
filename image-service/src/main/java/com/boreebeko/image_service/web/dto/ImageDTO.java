package com.boreebeko.image_service.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ImageDTO {

    private UUID id;
    private String imageName;
    private String contentType;
    private String url;
    private String classifiedLabel;
    private String processedAt;
}
