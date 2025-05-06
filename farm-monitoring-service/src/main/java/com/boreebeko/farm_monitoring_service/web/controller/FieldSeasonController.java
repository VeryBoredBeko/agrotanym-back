package com.boreebeko.farm_monitoring_service.web.controller;

import com.boreebeko.farm_monitoring_service.dto.FieldSeasonDTO;
import com.boreebeko.farm_monitoring_service.service.FieldSeasonService;
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

@Tag(name = "Field's seasonal data managing controller",
        description = "Controller provides endpoints for creating, updating, fetching and deleting field's seasonal data records.")
@RestController
@RequestMapping("/fields/{fieldId}/seasons")
public class FieldSeasonController {

    private final FieldSeasonService fieldSeasonService;

    @Autowired
    public FieldSeasonController(FieldSeasonService fieldSeasonService) {
        this.fieldSeasonService = fieldSeasonService;
    }

    @Operation(
            summary = "Get all seasonal data of specific field",
            description = "Returns list FieldSeasonDTO objects"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully fetched paginated seasonal data of specific field")
    })
    @GetMapping
    public ResponseEntity<List<FieldSeasonDTO>> getAllSeasons(@PathVariable Long fieldId, int page) {
        return new ResponseEntity<>(fieldSeasonService.getAllSeasons(page, fieldId), HttpStatus.OK);
    }

    @Operation(
            summary = "Creates new seasonal data for specified field"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created new seasonal data for specific field")
    })
    @PostMapping
    public ResponseEntity<FieldSeasonDTO> createFieldSeasonRecord(@PathVariable Long fieldId,
                                                                  @Validated @RequestBody FieldSeasonDTO fieldSeasonDTO) {
        FieldSeasonDTO response = fieldSeasonService.createFieldSeasonRecord(fieldId, fieldSeasonDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Deletes specified seasonal data record"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted specified seasonal data of specific field")
    })
    @DeleteMapping("/{fieldSeasonId}")
    public ResponseEntity<Void> deleteFieldSeasonRecord(@PathVariable Long fieldId,
                                                        @PathVariable Long fieldSeasonId) {
        fieldSeasonService.deleteFieldSeasonRecordById(fieldId, fieldSeasonId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Updates specified seasonal data record"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated specified seasonal data of specific field")
    })
    @PutMapping("/{fieldSeasonId}")
    public ResponseEntity<FieldSeasonDTO> updateFieldSeasonRecord(@PathVariable Long fieldId,
                                                                  @PathVariable Long fieldSeasonId,
                                                                  @Validated @RequestBody FieldSeasonDTO fieldSeasonDTO) {
        FieldSeasonDTO response = fieldSeasonService.updateFieldSeasonRecord(fieldId, fieldSeasonId, fieldSeasonDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
