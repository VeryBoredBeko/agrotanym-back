package com.boreebeko.farm_monitoring_service.dto;

import com.boreebeko.farm_monitoring_service.domain.FieldRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
public class FieldRoleDTO {

    private UUID userId;

    private FieldRole.Role role;
}
