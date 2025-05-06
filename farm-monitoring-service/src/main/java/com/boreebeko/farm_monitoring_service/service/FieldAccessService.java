package com.boreebeko.farm_monitoring_service.service;

import com.boreebeko.farm_monitoring_service.domain.FieldRole;
import com.boreebeko.farm_monitoring_service.repository.FieldRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service for checking user access to fields.
 *
 * <p>
 *     This service provides operations for checking user access to a specified field.
 * </p>
 *
 * @author Beknur Tumenov
 * @since 06.05.2025
 * */
@Service
public class FieldAccessService {

    private final FieldRoleRepository fieldRoleRepository;

    @Autowired
    public FieldAccessService(FieldRoleRepository fieldRoleRepository) {
        this.fieldRoleRepository = fieldRoleRepository;
    }

    /**
     * Checks is user the owner of specified field
     * @param fieldId The unique ID of field
     * @param userId The unique ID of user
     * @return true if user is the owner of field, otherwise false
     */
    public boolean isOwner(Long fieldId, UUID userId) {
        return fieldRoleRepository.findByFieldIdAndUserId(fieldId, userId)
                .map(role -> role.getRole() == FieldRole.Role.OWNER)
                .orElse(false);
    }

    public boolean isWorker(Long fieldId, UUID userId) {
        return fieldRoleRepository.findByFieldIdAndUserId(fieldId, userId)
                .map(role -> role.getRole() == FieldRole.Role.WORKER)
                .orElse(false);
    }

    /**
     * Checks is user related to a field
     * @param fieldId The unique ID of field
     * @param userId The unique ID of user
     * @return true, if user is related to field as a worker or owner, otherwise false
     */
    boolean isRelatedToField(Long fieldId, UUID userId) {
        return fieldRoleRepository.findByFieldIdAndUserId(fieldId, userId)
                .map(access -> access.getRole() == FieldRole.Role.OWNER || access.getRole() == FieldRole.Role.WORKER)
                .orElse(false);
    }

    /**
     * Checks is user has access to view field details
     * @param fieldId The unique ID of field
     * @param userId The unique ID of user
     * @return true, is user has access to view the field, otherwise false
     */
    public boolean canViewField(Long fieldId, UUID userId) {
        return fieldRoleRepository.findByFieldIdAndUserId(fieldId, userId).isPresent();
    }
}
