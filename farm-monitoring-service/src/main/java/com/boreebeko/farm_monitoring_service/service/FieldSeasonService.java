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

    @Transactional
    public void deleteFieldSeasonRecordById(Long fieldId, Long fieldSeasonId) {

        UUID currentUserId = UUID.fromString(userService.getUserId());

        if (!fieldAccessService.isOwner(fieldId, currentUserId))
            throw new AccessDeniedException("You don't have access to delete field's seasonal record.");

        if (!fieldSeasonRepository.existsById(fieldSeasonId))
            throw new FieldSeasonNotFoundException("There is no such field's seasonal record.");

        fieldSeasonRepository.deleteById(fieldSeasonId);
    }
}
