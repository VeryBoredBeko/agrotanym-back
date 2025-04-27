package com.boreebeko.farm_monitoring_service.mapper;

import com.boreebeko.farm_monitoring_service.domain.Field;
import com.boreebeko.farm_monitoring_service.dto.CoordinateDTO;
import com.boreebeko.farm_monitoring_service.dto.FieldDTO;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface FieldMapper extends Mappable<Field, FieldDTO> {
    FieldMapper INSTANCE = Mappers.getMapper(FieldMapper.class);

    @Mapping(source = "territory", target = "coordinates", qualifiedByName = "polygonToCoordinateDTO")
    @Override
    FieldDTO toDTO(Field field);

    @Named("polygonToCoordinateDTO")
    default List<CoordinateDTO> polygonToCoordinateDTO(Polygon polygon) {
        List<CoordinateDTO> coordinateDTOList = new ArrayList<>();

        if (polygon.getCoordinates() == null) throw new IllegalStateException();

        for (Coordinate coordinate : polygon.getCoordinates()) {
            coordinateDTOList.add(new CoordinateDTO(coordinate.x, coordinate.y));
        }

        return coordinateDTOList;
    }
}
