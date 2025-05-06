package com.boreebeko.farm_monitoring_service.repository;

import com.boreebeko.farm_monitoring_service.domain.FieldRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FieldRoleRepository extends JpaRepository<FieldRole, Long> {

    Optional<FieldRole> findByFieldIdAndUserId(Long fieldId, UUID userId);

    boolean existsByFieldIdAndUserIdAndRole(Long fieldId, UUID userId, FieldRole.Role role);

    @Query("SELECT fr FROM FieldRole fr WHERE fr.field.id = :fieldId")
    List<FieldRole> findAllByFieldId(@Param("fieldId") Long fieldId);

    @Modifying
    @Query("DELETE FROM FieldRole fr WHERE fr.field.id = :fieldId AND fr.userId = :userId")
    void deleteByFieldIdAndUserId(@Param("fieldId") Long fieldId, @Param("userId") UUID userId);

    @Query("SELECT fr.field.id FROM FieldRole fr WHERE fr.userId = :userId")
    Iterable<Long> findAllRelatedFieldsByUserId(@Param("userId") UUID userId);

    @Modifying
    @Query("DELETE FROM FieldRole fr WHERE fr.field.id = :fieldId")
    void deleteAllRelatedUsersByFieldId(@Param("fieldId") Long fieldId);
}
