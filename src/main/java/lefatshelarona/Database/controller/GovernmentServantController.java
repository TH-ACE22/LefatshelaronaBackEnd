package lefatshelarona.Database.controller;

import lefatshelarona.Database.model.GovernmentServant;
import lefatshelarona.Database.service.GovernmentServantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/government-servants")
@Tag(name = "Government Servant Management", description = "Operations related to government servants")
public class GovernmentServantController {

    private final GovernmentServantService service;

    public GovernmentServantController(GovernmentServantService service) {
        this.service = service;
    }

    @Operation(summary = "Get all government servants", description = "Retrieves all government servants.")
    @ApiResponse(responseCode = "200", description = "Government servants retrieved successfully")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @GetMapping
    public ResponseEntity<List<GovernmentServant>> getAllGovernmentServants() {
        return ResponseEntity.ok(service.getAllGovernmentServants());
    }

    @Operation(summary = "Get a government servant by ID", description = "Fetches details of a specific government servant.")
    @ApiResponse(responseCode = "200", description = "Government servant found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GovernmentServant.class)))
    @ApiResponse(responseCode = "404", description = "Government servant not found")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @GetMapping("/{id}")
    public ResponseEntity<GovernmentServant> getGovernmentServantById(@PathVariable String id) {
        return service.getGovernmentServantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get a government servant by email", description = "Fetches a government servant using their email address.")
    @ApiResponse(responseCode = "200", description = "Government servant found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GovernmentServant.class)))
    @ApiResponse(responseCode = "404", description = "Government servant not found")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @GetMapping("/email/{email}")
    public ResponseEntity<GovernmentServant> getGovernmentServantByEmail(@PathVariable String email) {
        return service.getGovernmentServantByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a government servant", description = "Adds a new government servant to the system.")
    @ApiResponse(responseCode = "201", description = "Government servant created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GovernmentServant.class)))
    @ApiResponse(responseCode = "400", description = "Invalid request body")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @PostMapping
    public ResponseEntity<GovernmentServant> createGovernmentServant(@RequestBody GovernmentServant servant) {
        return ResponseEntity.ok(service.createGovernmentServant(servant));
    }

    @Operation(summary = "Update a government servant", description = "Updates details of an existing government servant.")
    @ApiResponse(responseCode = "200", description = "Government servant updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GovernmentServant.class)))
    @ApiResponse(responseCode = "404", description = "Government servant not found")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @PutMapping("/{id}")
    public ResponseEntity<GovernmentServant> updateGovernmentServant(@PathVariable String id, @RequestBody GovernmentServant updatedServant) {
        try {
            return ResponseEntity.ok(service.updateGovernmentServant(id, updatedServant));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a government servant", description = "Deletes a government servant by their ID.")
    @ApiResponse(responseCode = "204", description = "Government servant deleted successfully")
    @ApiResponse(responseCode = "404", description = "Government servant not found")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGovernmentServant(@PathVariable String id) {
        service.deleteGovernmentServant(id);
        return ResponseEntity.noContent().build();
    }
}
