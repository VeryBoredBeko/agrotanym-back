package com.boreebeko.farm_monitoring_service.service;

import com.boreebeko.farm_monitoring_service.domain.Field;
import com.boreebeko.farm_monitoring_service.domain.FieldRole;
import com.boreebeko.farm_monitoring_service.domain.exception.AccessDeniedException;
import com.boreebeko.farm_monitoring_service.dto.FieldRoleDTO;
import com.boreebeko.farm_monitoring_service.mapper.FieldRoleMapper;
import com.boreebeko.farm_monitoring_service.repository.FieldRepository;
import com.boreebeko.farm_monitoring_service.repository.FieldRoleRepository;
import com.boreebeko.farm_monitoring_service.service.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing users access to farm fields.
 *
 * <p>
 *     This service provides operations for adding, fetching and removing user to have an access to a specified field.
 * </p>
 *
 * @author Beknur Tumenov
 * @since 06.05.2025
 * */
@Service
public class FieldUserManagementService {

    private final FieldRepository fieldRepository;
    private final FieldRoleRepository fieldRoleRepository;
    private final FieldAccessService fieldAccessService;

    private final UserService userService;

    private final FieldRoleMapper fieldRoleMapper = FieldRoleMapper.INSTANCE;

    @Autowired
    public FieldUserManagementService(FieldRepository fieldRepository, FieldRoleRepository fieldRoleRepository, FieldAccessService fieldAccessService, UserService userService) {
        this.fieldRepository = fieldRepository;
        this.fieldRoleRepository = fieldRoleRepository;
        this.fieldAccessService = fieldAccessService;
        this.userService = userService;
    }

    /**
     * Adds user as a worker to a specified field.
     * @param fieldId The unique ID of field.
     * @param targetUserId The unique ID of user.
     *
     * @throws AccessDeniedException if the user which is calling the operation is not an owner of specified field.
     * @throws IllegalStateException if the target user is already added as a worker to a specified field.
     */
    @Transactional
    public void addWorker(Long fieldId, UUID targetUserId) {

        UUID currentUserId = UUID.fromString(userService.getUserId());

        if (!fieldAccessService.isOwner(fieldId, currentUserId))
            throw new AccessDeniedException("Only the owner can add workers");

        fieldRoleRepository.findByFieldIdAndUserId(fieldId, targetUserId)
                .ifPresent(role -> {
                    throw new IllegalStateException("User already added to field");
                });

        FieldRole role = new FieldRole();

        Field field = fieldRepository.getReferenceById(fieldId);

        role.setField(field);
        role.setUserId(targetUserId);
        role.setRole(FieldRole.Role.WORKER);

        FieldRole persistedEntity = fieldRoleRepository.save(role);
        fieldRoleMapper.toDTO(persistedEntity);
    }

    /**
     * Returns the list of users who has access to a specified field.
     * @param fieldId The unique ID of field.
     * @return list of {@link FieldRoleDTO} instances.
     *
     * @throws AccessDeniedException if the user which is calling the operation is not an owner of specified field.
     */
    @Transactional(readOnly = true)
    public List<FieldRoleDTO> getAllUsers(Long fieldId) {

        UUID currentUserId = UUID.fromString(userService.getUserId());

        if (!fieldAccessService.isOwner(fieldId, currentUserId))
            throw new AccessDeniedException("Only the owner can view field workers");

        return fieldRoleMapper.toDTOList(fieldRoleRepository.findAllByFieldId(fieldId));
    }

    /**
     * Removes a user from a specified field.
     * @param fieldId The unique ID of field
     * @param targetUserId The unique ID of user
     *
     * @throws AccessDeniedException if the user which is calling the operation is not an owner of specified field.
     * @throws IllegalStateException if the user which is calling the operation is trying to delete himself.
     */
    @Transactional
    public void removeUser(Long fieldId, UUID targetUserId) {

        UUID currentUserId = UUID.fromString(userService.getUserId());

        if (!fieldAccessService.isOwner(fieldId, currentUserId))
            throw new AccessDeniedException("Only the owner can remove worker from field");

        if (currentUserId.compareTo(targetUserId) == 0)
            throw new IllegalStateException("You can't remove yourself");

        fieldRoleRepository.deleteByFieldIdAndUserId(fieldId, targetUserId);
    }
}
