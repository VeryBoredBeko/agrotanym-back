package com.boreebeko.farm_monitoring_service.web.controller;

import com.boreebeko.farm_monitoring_service.dto.FieldRoleDTO;
import com.boreebeko.farm_monitoring_service.service.FieldUserManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(
        name = "Field's user management controller",
        description = "Controller provides endpoints for adding, fetching and removing user's access to a specific field"
)
@RestController
@RequestMapping(value = "/fields/{fieldId}/users")
public class FieldUserManagementController {

    private final FieldUserManagementService fieldUserManagementService;

    @Autowired
    public FieldUserManagementController(FieldUserManagementService fieldUserManagementService) {
        this.fieldUserManagementService = fieldUserManagementService;
    }

    @Operation(
            summary = "Fetches all users who has access to a specific field"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "All users was fetched successfully")
    })
    @GetMapping
    public ResponseEntity<List<FieldRoleDTO>> getAllWorkersByFieldId(@PathVariable Long fieldId) {
        return new ResponseEntity<>(fieldUserManagementService.getAllUsers(fieldId), HttpStatus.OK);
    }

    @Operation(
            summary = "Adds a specific user for giving him access to a view and modifying specific field"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User was added successfully")
    })
    @PostMapping
    public ResponseEntity<Void> addWorkerToField(@PathVariable Long fieldId, @RequestParam String userId) {
        fieldUserManagementService.addWorker(fieldId, UUID.fromString(userId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Removes user from having access to a specific field"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User was removed successfully")
    })
    @DeleteMapping
    public ResponseEntity<Void> deleteWorkerFromField(@PathVariable Long fieldId, @RequestParam String userId) {
        fieldUserManagementService.removeUser(fieldId, UUID.fromString(userId));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
