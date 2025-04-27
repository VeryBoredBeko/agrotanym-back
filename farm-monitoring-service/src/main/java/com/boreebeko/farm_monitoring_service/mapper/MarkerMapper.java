package com.boreebeko.farm_monitoring_service.mapper;

import com.boreebeko.farm_monitoring_service.domain.Marker;
import com.boreebeko.farm_monitoring_service.dto.CoordinateDTO;
import com.boreebeko.farm_monitoring_service.dto.MarkerDTO;
import org.locationtech.jts.geom.Point;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MarkerMapper extends Mappable<Marker, MarkerDTO> {
    MarkerMapper INSTANCE = Mappers.getMapper(MarkerMapper.class);

    @Mapping(source = "location", target = "coordinate", qualifiedByName = "locationToCoordinateDTO")
    @Override
    MarkerDTO toDTO(Marker marker);

    @Named("locationToCoordinateDTO")
    default CoordinateDTO polygonToCoordinateDTO(Point location) {
        if (location.getCoordinates() == null) throw new IllegalStateException();
        return new CoordinateDTO(location.getX(), location.getY());
    }
}
