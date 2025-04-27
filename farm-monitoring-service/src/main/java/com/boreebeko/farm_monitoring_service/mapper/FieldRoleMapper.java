package com.boreebeko.farm_monitoring_service.mapper;

import com.boreebeko.farm_monitoring_service.domain.FieldRole;
import com.boreebeko.farm_monitoring_service.dto.FieldRoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FieldRoleMapper extends Mappable<FieldRole, FieldRoleDTO> {

    FieldRoleMapper INSTANCE = Mappers.getMapper(FieldRoleMapper.class);
}
