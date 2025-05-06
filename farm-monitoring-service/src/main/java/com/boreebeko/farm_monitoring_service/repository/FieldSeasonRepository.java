package com.boreebeko.farm_monitoring_service.repository;

import com.boreebeko.farm_monitoring_service.domain.FieldSeason;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldSeasonRepository extends JpaRepository<FieldSeason, Long> {

    Page<FieldSeason> findByFieldId(Long fieldId, Pageable pageable);

    @Modifying
    @Query("DELETE FROM FieldSeason fs WHERE fs.field.id = :fieldId")
    void deleteAllRelatedSeasonRecordsByFieldId(@Param("fieldId") Long fieldId);
}
