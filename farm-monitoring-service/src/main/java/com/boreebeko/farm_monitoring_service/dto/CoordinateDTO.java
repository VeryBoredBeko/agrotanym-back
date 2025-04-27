package com.boreebeko.farm_monitoring_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CoordinateDTO {

    private double longitude;
    private double latitude;
}
