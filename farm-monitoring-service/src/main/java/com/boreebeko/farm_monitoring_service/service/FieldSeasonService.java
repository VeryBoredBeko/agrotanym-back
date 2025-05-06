package com.boreebeko.farm_monitoring_service.service;

import com.boreebeko.farm_monitoring_service.domain.Field;
import com.boreebeko.farm_monitoring_service.domain.FieldSeason;
import com.boreebeko.farm_monitoring_service.domain.exception.AccessDeniedException;
import com.boreebeko.farm_monitoring_service.domain.exception.FieldNotFoundException;
import com.boreebeko.farm_monitoring_service.domain.exception.FieldSeasonNotFoundException;
import com.boreebeko.farm_monitoring_service.dto.FieldSeasonDTO;
import com.boreebeko.farm_monitoring_service.mapper.FieldSeasonMapper;
import com.boreebeko.farm_monitoring_service.repository.FieldRepository;
import com.boreebeko.farm_monitoring_service.repository.FieldSeasonRepository;
import com.boreebeko.farm_monitoring_service.service.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing seasonal data about farm fields.
 *
 * <p>
 *     This service provides operations for creating, fetching and removing seasonal data about a specified field.
 * </p>
 *
 * @author Beknur Tumenov
 * @since 06.05.2025
 * */
@Service
public class FieldSeasonService {

    private final FieldRepository fieldRepository;
    private final FieldSeasonRepository fieldSeasonRepository;
    private final FieldSeasonMapper fieldSeasonMapper = FieldSeasonMapper.INSTANCE;

    private final UserService userService;

    private static final int PAGE_SIZE = 10;

    private final FieldAccessService fieldAccessService;

    @Autowired
    public FieldSeasonService(FieldRepository fieldRepository,
                              FieldSeasonRepository fieldSeasonRepository,
                              UserService userService,
                              FieldAccessService fieldAccessService) {

        this.fieldRepository = fieldRepository;
        this.fieldSeasonRepository = fieldSeasonRepository;
        this.userService = userService;
        this.fieldAccessService = fieldAccessService;
    }

    /**
     * Fetches paginated seasonal data about a specified field.
     * @param page The number of page
     * @param fieldId The unique ID of field
     * @return list of {@link FieldSeasonDTO} instances, which represents paginated the existing seasonal data about specified field.
     *
     * @throws AccessDeniedException if the user which is calling the operation is not an owner of specified field.
     * @throws FieldNotFoundException if there is no existing field with such ID.
     */
    @Transactional(readOnly = true)
    public List<FieldSeasonDTO> getAllSeasons(int page, Long fieldId) {

        UUID currentUserId = UUID.fromString(userService.getUserId());

        if (!fieldAccessService.isRelatedToField(fieldId, currentUserId))
            throw new AccessDeniedException("You don't have access to view the field's seasonal records.");

        if (!fieldRepository.existsById(fieldId))
            throw new FieldNotFoundException("There is no field with such ID.");

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return fieldSeasonMapper.toDTOList(fieldSeasonRepository.findByFieldId(fieldId, pageable).stream().toList());
    }

    /**
     * Creates a seasonal data about a specified field.
     * @param fieldId The unique ID of field
     * @param fieldSeasonDTO The DTO object which represents seasonal data
     * @return mapped instance of {@link FieldSeasonDTO} class, which is persisted in database.
     *
     * @throws AccessDeniedException if the user which is calling the operation is not related to a specified field.
     */
    @Transactional
    public FieldSeasonDTO createFieldSeasonRecord(Long fieldId, FieldSeasonDTO fieldSeasonDTO) {

        UUID currentUserId = UUID.fromString(userService.getUserId());

        if (!fieldAccessService.isRelatedToField(fieldId, currentUserId))
            throw new AccessDeniedException("You don't have access to create field season record.");

        FieldSeason mappedEntity = fieldSeasonMapper.toEntity(fieldSeasonDTO);
        mappedEntity.setUserId(currentUserId);

        Field persistedField = fieldRepository.findById(fieldId).orElseThrow(FieldNotFoundException::new);

        mappedEntity.setField(persistedField);

        FieldSeason persistedEntity = fieldSeasonRepository.save(mappedEntity);
        return fieldSeasonMapper.toDTO(persistedEntity);
    }

    /**
     * Deletes seasonal data about related to a specified field.
     * @param fieldId The unique ID of field
     * @param fieldSeasonId The unique ID of seasonal data record
     *
     * @throws AccessDeniedException if the user which is calling the operation is not an owner of specified field.
     * @throws FieldSeasonNotFoundException if there is no seasonal data with such ID.
     */
    @Transactional
    public void deleteFieldSeasonRecordById(Long fieldId, Long fieldSeasonId) {

        UUID currentUserId = UUID.fromString(userService.getUserId());

        if (!fieldAccessService.isOwner(fieldId, currentUserId))
            throw new AccessDeniedException("You don't have access to delete field's seasonal record.");

        if (!fieldSeasonRepository.existsById(fieldSeasonId))
            throw new FieldSeasonNotFoundException("There is no such field's seasonal record.");

        fieldSeasonRepository.deleteById(fieldSeasonId);
    }

    /**
     * Updates specified seasonal data record.
     * @param fieldId The unique ID of field
     * @param fieldSeasonId The unique ID field seasonal data record
     * @param fieldSeasonDTO The DTO instance, which represents updated record
     * @return updated seasonal data record as DTO instance
     *
     * @throws AccessDeniedException if the user which is calling the operation is not related to a specified field
     * and is not the user who created the record or owner of the field.
     * @throws FieldSeasonNotFoundException if there is no such record with specified ID.
     */
    public FieldSeasonDTO updateFieldSeasonRecord(Long fieldId, Long fieldSeasonId, FieldSeasonDTO fieldSeasonDTO) {

        UUID currentUserId = UUID.fromString(userService.getUserId());

        if (!fieldAccessService.isRelatedToField(fieldId, currentUserId))
            throw new AccessDeniedException("You don't have access to update field season record.");

        FieldSeason specifiedFieldSeasonRecord =
                fieldSeasonRepository.findById(fieldSeasonId).orElseThrow(FieldSeasonNotFoundException::new);

        if (specifiedFieldSeasonRecord.getUserId().compareTo(currentUserId) != 0 ||
                !fieldAccessService.isOwner(fieldId, currentUserId))
            throw new AccessDeniedException("You don't have access to update field season record");

        // Updating already existing field season record
        specifiedFieldSeasonRecord.setCrop(fieldSeasonDTO.getCrop());
        specifiedFieldSeasonRecord.setYear(fieldSeasonDTO.getYear());
        specifiedFieldSeasonRecord.setYield(fieldSeasonDTO.getYield());
        specifiedFieldSeasonRecord.setTreatments(fieldSeasonDTO.getTreatments());

        FieldSeason updatedFieldSeasonRecord = fieldSeasonRepository.save(specifiedFieldSeasonRecord);
        return fieldSeasonMapper.toDTO(updatedFieldSeasonRecord);
    }
}
