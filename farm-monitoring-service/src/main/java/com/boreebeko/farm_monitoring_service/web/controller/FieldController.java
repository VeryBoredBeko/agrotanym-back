package com.boreebeko.farm_monitoring_service.web.controller;

import com.boreebeko.farm_monitoring_service.dto.FieldDTO;
import com.boreebeko.farm_monitoring_service.dto.MarkerDTO;
import com.boreebeko.farm_monitoring_service.service.FieldService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Farm field managing controller", description = "API for creating, fetching and managing farm fields")
@RestController
public class FieldController {

    private final FieldService fieldService;

    @Autowired
    public FieldController(FieldService fieldService) {
        this.fieldService = fieldService;
    }

    @Operation(
            summary = "Get all fields",
            description = "Returns list of FieldDTO objects which was created by user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched all user created fields successfully")
    })
    @GetMapping("/fields")
    public List<FieldDTO> getAllFarms() {
        return fieldService.getAllFields();
    }

    @Operation(
            summary = "Create new farm field",
            description = "Creating new farm field. Function will wait FieldDTO class object to store it."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Field created successfully"),
            @ApiResponse(responseCode = "400", description = "Field wasn't created")
    })
    @PostMapping("/fields")
    public FieldDTO createField(@Validated @RequestBody FieldDTO fieldDTO) {
        return fieldService.createField(fieldDTO);
    }

    @Operation(
            summary = "Get in-depth information about field",
            description = "Returns all related information about given field."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Field information fetched successfully"),
            @ApiResponse(responseCode = "400", description = "There is no field with such ID")
    })
    @GetMapping("/fields/{fieldId}")
    public ResponseEntity<FieldDTO> getFieldById(@PathVariable Long fieldId) {
        return new ResponseEntity<>(fieldService.getFieldById(fieldId), HttpStatus.valueOf(401));
    }

    @Operation(
            summary = "Fetch all markers which contained in given field",
            description = "Returns all markers information contained in given field."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Markers information fetched successfully"),
            @ApiResponse(responseCode = "400", description = "There is no field with such ID")
    })
    @GetMapping("/fields/{fieldId}/markers")
    public ResponseEntity<List<MarkerDTO>> getAllMarkersByFieldId(@PathVariable Long fieldId) {
        return new ResponseEntity<>(fieldService.getAllMarkersByFieldId(fieldId), HttpStatus.OK);
    }

    @Operation(
            summary = "Create a new marker in given field"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marker was created successfully"),
            @ApiResponse(responseCode = "400", description = "Marker wasn't created")
    })
    @PostMapping("/fields/{fieldId}/markers")
    public ResponseEntity<MarkerDTO> createMarkerByFieldId(@PathVariable Long fieldId, @Validated @RequestBody MarkerDTO markerDTO) {
        return new ResponseEntity<>(fieldService.createMarkerByFieldId(fieldId, markerDTO), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete field by given ID",
            description = "Delete all information related to given field"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Field information deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Field information wasn't deleted successfully")
    })
    @DeleteMapping("/fields/{fieldId}")
    public ResponseEntity<Void> deleteFieldById(@PathVariable Long fieldId) {
        fieldService.deleteFieldById(fieldId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/fields/{fieldId}/markers/{markerId}")
    public ResponseEntity<Void> deleteMarkerById(@PathVariable Long fieldId, @PathVariable Long markerId) {
        fieldService.deleteMarkerById(fieldId, markerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
