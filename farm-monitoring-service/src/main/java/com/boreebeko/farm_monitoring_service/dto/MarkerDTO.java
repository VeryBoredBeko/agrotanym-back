package com.boreebeko.farm_monitoring_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@NoArgsConstructor
@Setter
@Getter
public class MarkerDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private UUID userId;

    private String name;
    private String description;

    private String imageURL;
    private MultipartFile imageFile;

    private CoordinateDTO coordinate;
}
