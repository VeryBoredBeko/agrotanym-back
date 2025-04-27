package com.boreebeko.farm_monitoring_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class FieldDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    @Size(min = 8, max = 255, message = "The field's name must be between 8 and 255 characters long")
    private String name;

    @NotBlank
    @Size(min = 2, max = 255, message = "The crop name must be between 2 and 255 characters long")
    private String crop;

    @Size(max = 255, message = "The hybrid name must be between 0 and 255 characters long")
    private String hybrid;

    private LocalDate sowingDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double area;

    @Size(max = 255, message = "The soil type must be between 0 and 255 characters long")
    private String soilType;

    @Size(max = 255, message = "The tillage type must be between 0 and 255 characters long")
    private String tillageType;

    private String manager;

    private List<CoordinateDTO> coordinates;

    private List<MarkerDTO> markers;
}
