package com.boreebeko.image_service.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "User uploaded image DTO")
@Data
@NoArgsConstructor
public class ImageDTO {

    @Schema(description = "Image ID", example = "24dc13c3-3e04-47a5-b096-a25d7df6ae7e")
    private UUID id;

    @Schema(description = "Unique image name", example = "24dc13c3-3e04-47a5-b096-a25d7df6ae7e")
    private String imageName;

    @Schema(description = "Image content type", example = "image/jpeg")
    private String contentType;

    @Schema(description = "Image location URL", example = "http://minio-ip/minio-port/24dc13c3-3e04-47a5-b096-a25d7df6ae7e")
    private String url;

    @Schema(description = "Image classification result label", example = "Apple Scar")
    private String classifiedLabel;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @Schema(description = "Image upload time", example = "2025-03-26T18:16:17.993699")
    private LocalDateTime processedAt;
}
