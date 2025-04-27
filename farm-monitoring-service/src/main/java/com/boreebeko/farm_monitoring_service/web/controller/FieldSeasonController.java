package com.boreebeko.farm_monitoring_service.web.controller;

import com.boreebeko.farm_monitoring_service.dto.FieldSeasonDTO;
import com.boreebeko.farm_monitoring_service.service.FieldSeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fields/{fieldId}/seasons")
public class FieldSeasonController {

    private final FieldSeasonService fieldSeasonService;

    @Autowired
    public FieldSeasonController(FieldSeasonService fieldSeasonService) {
        this.fieldSeasonService = fieldSeasonService;
    }

    @GetMapping
    public ResponseEntity<List<FieldSeasonDTO>> getAllSeasons(@PathVariable Long fieldId, int page) {
        return new ResponseEntity<>(fieldSeasonService.getAllSeasons(page, fieldId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<FieldSeasonDTO> createFieldSeasonRecord(@PathVariable Long fieldId,
                                                                  @Validated @RequestBody FieldSeasonDTO fieldSeasonDTO) {
        FieldSeasonDTO response = fieldSeasonService.createFieldSeasonRecord(fieldId, fieldSeasonDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{fieldSeasonId}")
    public ResponseEntity<Void> deleteFieldSeasonRecord(@PathVariable Long fieldId,
                                                        @PathVariable Long fieldSeasonId) {
        fieldSeasonService.deleteFieldSeasonRecordById(fieldId, fieldSeasonId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
