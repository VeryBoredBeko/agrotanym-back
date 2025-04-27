package com.boreebeko.farm_monitoring_service.repository;

import com.boreebeko.farm_monitoring_service.domain.Marker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarkerRepository extends JpaRepository<Marker, Long> {
    List<Marker> findByFieldId(Long fieldId);
}
