package com.boreebeko.farm_monitoring_service.repository;

import com.boreebeko.farm_monitoring_service.domain.Marker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarkerRepository extends JpaRepository<Marker, Long> {
    List<Marker> findByFieldId(Long fieldId);

    @Modifying
    @Query("DELETE FROM Marker m WHERE m.field.id = :fieldId")
    void deleteAllRelatedMarkersByFieldId(@Param("fieldId") Long fieldId);

}
