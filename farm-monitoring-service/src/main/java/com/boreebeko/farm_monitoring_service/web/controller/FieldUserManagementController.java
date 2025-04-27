package com.boreebeko.farm_monitoring_service.web.controller;

import com.boreebeko.farm_monitoring_service.dto.FieldRoleDTO;
import com.boreebeko.farm_monitoring_service.service.FieldUserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/fields/{fieldId}/users")
public class FieldUserManagementController {

    private final FieldUserManagementService fieldUserManagementService;

    @Autowired
    public FieldUserManagementController(FieldUserManagementService fieldUserManagementService) {
        this.fieldUserManagementService = fieldUserManagementService;
    }

    @GetMapping
    public ResponseEntity<List<FieldRoleDTO>> getAllWorkersByFieldId(@PathVariable Long fieldId) {
        return new ResponseEntity<>(fieldUserManagementService.getAllUsers(fieldId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addWorkerToField(@PathVariable Long fieldId, @RequestParam String userId) {
        fieldUserManagementService.addWorker(fieldId, UUID.fromString(userId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteWorkerFromField(@PathVariable Long fieldId, @RequestParam String userId) {
        fieldUserManagementService.removeUser(fieldId, UUID.fromString(userId));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
