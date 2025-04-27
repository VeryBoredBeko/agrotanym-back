package com.boreebeko.farm_monitoring_service.repository;

import com.boreebeko.farm_monitoring_service.domain.Field;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FieldRepository extends JpaRepository<Field, Long> {

    @Query(value = "SELECT ST_Area(CAST(:polygon AS geography))", nativeQuery = true)
    Double calculateAreaFromGeometry(@Param("polygon") Polygon polygon);

    @Query("SELECT f.ownerId FROM Field f WHERE f.id = :fieldId")
    UUID getOwnerOfTheField(@Param("fieldId") Long fieldId);
}
