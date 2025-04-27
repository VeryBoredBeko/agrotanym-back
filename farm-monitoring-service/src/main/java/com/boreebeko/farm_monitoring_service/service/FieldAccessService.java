package com.boreebeko.farm_monitoring_service.service;

import com.boreebeko.farm_monitoring_service.domain.FieldRole;
import com.boreebeko.farm_monitoring_service.repository.FieldRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FieldAccessService {

    private final FieldRoleRepository fieldRoleRepository;

    @Autowired
    public FieldAccessService(FieldRoleRepository fieldRoleRepository) {
        this.fieldRoleRepository = fieldRoleRepository;
    }

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

    boolean isRelatedToField(Long fieldId, UUID userId) {
        return fieldRoleRepository.findByFieldIdAndUserId(fieldId, userId)
                .map(access -> access.getRole() == FieldRole.Role.OWNER || access.getRole() == FieldRole.Role.WORKER)
                .orElse(false);
    }

    public boolean canViewField(Long fieldId, UUID userId) {
        return fieldRoleRepository.findByFieldIdAndUserId(fieldId, userId).isPresent();
    }
}
