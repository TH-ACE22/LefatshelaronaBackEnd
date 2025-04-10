package lefatshelarona.Database.controller;

import lefatshelarona.Database.model.GovernmentService;
import lefatshelarona.Database.service.GovernmentServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/government-services")
@Tag(name = "Government Service Management", description = "Operations related to government services")
public class GovernmentServiceController {
    private final GovernmentServiceService service;

    public GovernmentServiceController(GovernmentServiceService service) {
        this.service = service;
    }

    @Operation(summary = "Get all government services", description = "Retrieves all government services.")
    @ApiResponse(responseCode = "200", description = "Government services retrieved successfully")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @GetMapping
    public ResponseEntity<List<GovernmentService>> getAllServices() {
        return ResponseEntity.ok(service.getAllServices());
    }

    @Operation(summary = "Get a government service by ID", description = "Fetches details of a specific government service.")
    @ApiResponse(responseCode = "200", description = "Government service found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GovernmentService.class)))
    @ApiResponse(responseCode = "404", description = "Government service not found")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @GetMapping("/{id}")
    public ResponseEntity<GovernmentService> getServiceById(@PathVariable String id) {
        return service.getServiceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a government service", description = "Adds a new government service to the system.")
    @ApiResponse(responseCode = "201", description = "Government service created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GovernmentService.class)))
    @ApiResponse(responseCode = "400", description = "Invalid request body")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @PostMapping
    public ResponseEntity<GovernmentService> createService(@RequestBody GovernmentService governmentService) {
        return ResponseEntity.ok(service.createService(governmentService));
    }

    @Operation(summary = "Update a government service", description = "Updates details of an existing government service.")
    @ApiResponse(responseCode = "200", description = "Government service updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GovernmentService.class)))
    @ApiResponse(responseCode = "404", description = "Government service not found")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @PutMapping("/{id}")
    public ResponseEntity<GovernmentService> updateService(@PathVariable String id, @RequestBody GovernmentService governmentService) {
        return ResponseEntity.ok(service.updateService(id, governmentService));
    }

    @Operation(summary = "Delete a government service", description = "Deletes a government service by its ID.")
    @ApiResponse(responseCode = "204", description = "Government service deleted successfully")
    @ApiResponse(responseCode = "404", description = "Government service not found")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable String id) {
        service.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}
