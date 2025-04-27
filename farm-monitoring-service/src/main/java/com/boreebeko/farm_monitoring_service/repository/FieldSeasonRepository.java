package com.boreebeko.farm_monitoring_service.repository;

import com.boreebeko.farm_monitoring_service.domain.FieldSeason;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldSeasonRepository extends JpaRepository<FieldSeason, Long> {

    Page<FieldSeason> findByFieldId(Long fieldId, Pageable pageable);
}
