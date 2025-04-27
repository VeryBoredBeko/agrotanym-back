package com.boreebeko.farm_monitoring_service.service;

import com.boreebeko.farm_monitoring_service.domain.Field;
import com.boreebeko.farm_monitoring_service.domain.FieldRole;
import com.boreebeko.farm_monitoring_service.domain.Marker;
import com.boreebeko.farm_monitoring_service.domain.exception.AccessDeniedException;
import com.boreebeko.farm_monitoring_service.dto.CoordinateDTO;
import com.boreebeko.farm_monitoring_service.dto.FieldDTO;
import com.boreebeko.farm_monitoring_service.dto.MarkerDTO;
import com.boreebeko.farm_monitoring_service.mapper.FieldMapper;
import com.boreebeko.farm_monitoring_service.mapper.MarkerMapper;
import com.boreebeko.farm_monitoring_service.repository.FieldRepository;
import com.boreebeko.farm_monitoring_service.repository.FieldRoleRepository;
import com.boreebeko.farm_monitoring_service.repository.MarkerRepository;
import com.boreebeko.farm_monitoring_service.service.auth.UserService;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing user farm fields using PostgresSQL Database
 *
 * <p>
 *     This service provides operations to create, delete and fetch fields,
 *     it's information and markers.
 * </p>
 *
 * @author Tumenov Beknur
 * @version 1.0
 * @since 02.04.2025
 * */
@Service
public class FieldService {

    private final FieldRepository fieldRepository;
    private final MarkerRepository markerRepository;

    private final FieldMapper fieldMapper = FieldMapper.INSTANCE;
    private final MarkerMapper markerMapper = MarkerMapper.INSTANCE;

    private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    private final UserService userService;

    private final FieldAccessService fieldAccessService;

    private final FieldRoleRepository fieldRoleRepository;

    @Autowired
    public FieldService(FieldRepository fieldRepository, MarkerRepository markerRepository, UserService userService, FieldAccessService fieldAccessService, FieldRoleRepository fieldRoleRepository) {
        this.fieldRepository = fieldRepository;
        this.markerRepository = markerRepository;
        this.userService = userService;
        this.fieldAccessService = fieldAccessService;
        this.fieldRoleRepository = fieldRoleRepository;
    }

    /**
     * Creates user's new farm field
     *
     * @param fieldDTO FieldDTO class object uploaded by user, which represents field information
     * @throws IllegalArgumentException if field has invalid polygon form
     * @return FieldDTO mapping of newly created field
     * */
    public FieldDTO createField(FieldDTO fieldDTO) {

        UUID currentUserId = UUID.fromString(userService.getUserId());

        Field field = fieldMapper.toEntity(fieldDTO);
        field.setOwnerId(currentUserId);

        List<CoordinateDTO> coordinateDTOs = fieldDTO.getCoordinates();

        if (coordinateDTOs == null || coordinateDTOs.size() < 4) {
            throw new IllegalArgumentException("Polygon must have at least 4 coordinates (including closing point)");
        }

        Coordinate[] coordinates = coordinateDTOs.stream()
                .map(dto -> new Coordinate(dto.getLongitude(), dto.getLatitude()))
                .toArray(Coordinate[]::new);

        LinearRing shell = geometryFactory.createLinearRing(coordinates);

        Polygon polygon = geometryFactory.createPolygon(shell, null);
        polygon.setSRID(4326);

        Double area = fieldRepository.calculateAreaFromGeometry(polygon);

        field.setTerritory(polygon);
        field.setArea(area);

        Field persistedEntity = fieldRepository.save(field);

        FieldRole fieldRole = new FieldRole();
        fieldRole.setUserId(currentUserId);
        fieldRole.setField(persistedEntity);
        fieldRole.setRole(FieldRole.Role.OWNER);

        fieldRoleRepository.save(fieldRole);

        return fieldMapper.toDTO(persistedEntity);
    }

    /**
     * Uploads user image to a database and MinIO storage
     *
     * @param fieldId The unique ID of field
     * @throws IllegalArgumentException if given field doesn't contain marker's position
     * @return MarkerDTO mapping of newly created marker
     * */
    public MarkerDTO createMarkerByFieldId(Long fieldId, MarkerDTO markerDTO) {

        UUID currentUserId = UUID.fromString(userService.getUserId());

        if (!fieldAccessService.isRelatedToField(fieldId, currentUserId))
            throw new AccessDeniedException("You don't have access to add marker to this field.");

        Coordinate coordinate = new Coordinate(markerDTO.getCoordinate().getLongitude(), markerDTO.getCoordinate().getLatitude());
        Point markerLocation = geometryFactory.createPoint(coordinate);

        Marker newMarker = markerMapper.toEntity(markerDTO);
        newMarker.setLocation(markerLocation);
        newMarker.setUserId(currentUserId);

        Field persistedField = fieldRepository.findById(fieldId).orElseThrow(IllegalArgumentException::new);

        if (!persistedField.getTerritory().contains(markerLocation)) throw new IllegalArgumentException("Invalid Marker");

        newMarker.setField(persistedField);

        Marker persistedMarker = markerRepository.save(newMarker);

        return markerMapper.toDTO(persistedMarker);
    }

    /**
     * Fetches a list of markers contained in field
     *
     * @param fieldId The unique ID of field
     * @return List of MarkerDTO mappings which contained in field
     * */
    public List<MarkerDTO> getAllMarkersByFieldId(Long fieldId) {
        UUID currentUserId = UUID.fromString(userService.getUserId());

        if (!fieldAccessService.canViewField(fieldId, currentUserId))
            throw new AccessDeniedException();

        return markerMapper.toDTOList(markerRepository.findByFieldId(fieldId));
    }

    /**
     * Delete the marker contained in field
     *
     * @param fieldId The unique ID of field
     * */
    public void deleteFieldById(Long fieldId) {
        UUID currentUserId = UUID.fromString(userService.getUserId());

        if (fieldRepository.getOwnerOfTheField(fieldId).compareTo(currentUserId) != 0)
            throw new AccessDeniedException();

        fieldRepository.deleteById(fieldId);
    }

    /**
     * Fetches all fields which user is related
     *
     * @return List of FieldDTO mappings which user had created
     * */
    public List<FieldDTO> getAllFields() {
        UUID currentUserId = UUID.fromString(userService.getUserId());
        return fieldMapper.toDTOList(fieldRepository.findAllById(fieldRoleRepository.findAllRelatedFieldsByUserId(currentUserId)));
    }

    /**
     * Fetches field
     *
     * @param fieldId The unique ID of field
     * @throws IllegalArgumentException if there is no such field with given id
     * @return FieldDTO mapping of field
     * */
    public FieldDTO getFieldById(Long fieldId) {

        UUID currentUserId = UUID.fromString(userService.getUserId());

        if (!fieldAccessService.isRelatedToField(fieldId, currentUserId))
            throw new AccessDeniedException("You don't have access to view this field.");

        Field field = fieldRepository.findById(fieldId).orElseThrow(IllegalArgumentException::new);

        FieldDTO fieldDTO = fieldMapper.toDTO(field);
        List<MarkerDTO> markers = markerMapper.toDTOList(markerRepository.findByFieldId(fieldId));

        fieldDTO.setMarkers(markers);

        return fieldDTO;
    }

    public void deleteMarkerById(Long fieldId, Long markerId) {

        UUID currentUserId = UUID.fromString(userService.getUserId());

        if (!fieldAccessService.isOwner(fieldId, currentUserId))
            throw new AccessDeniedException();

        markerRepository.deleteById(markerId);
    }
}
