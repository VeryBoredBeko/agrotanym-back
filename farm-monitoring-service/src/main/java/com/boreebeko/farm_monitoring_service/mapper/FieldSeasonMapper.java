package com.boreebeko.farm_monitoring_service.mapper;

import com.boreebeko.farm_monitoring_service.domain.FieldSeason;
import com.boreebeko.farm_monitoring_service.dto.FieldSeasonDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FieldSeasonMapper extends Mappable<FieldSeason, FieldSeasonDTO> {
    FieldSeasonMapper INSTANCE = Mappers.getMapper(FieldSeasonMapper.class);
}
