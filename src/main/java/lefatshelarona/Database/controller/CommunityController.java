package lefatshelarona.Database.controller;

import lefatshelarona.Database.model.Community;
import lefatshelarona.Database.service.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/communities")
@Tag(name = "Community Management", description = "Operations related to communities")
public class CommunityController {
    private final CommunityService communityService;

    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @Operation(summary = "Get all communities", description = "Retrieves a list of all communities.")
    @ApiResponse(responseCode = "200", description = "Communities retrieved successfully")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @GetMapping
    public ResponseEntity<List<Community>> getAllCommunities() {
        return ResponseEntity.ok(communityService.getAllCommunities());
    }

    @Operation(summary = "Get a community by ID", description = "Fetches details of a specific community.")
    @ApiResponse(responseCode = "200", description = "Community found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Community.class)))
    @ApiResponse(responseCode = "404", description = "Community not found")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @GetMapping("/{id}")
    public ResponseEntity<Community> getCommunityById(@PathVariable String id) {
        return communityService.getCommunityById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a community", description = "Adds a new community to the system.")
    @ApiResponse(responseCode = "201", description = "Community created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Community.class)))
    @ApiResponse(responseCode = "400", description = "Invalid request body")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @PostMapping
    public ResponseEntity<Community> createCommunity(@RequestBody Community community) {
        return ResponseEntity.ok(communityService.createCommunity(community));
    }

    @Operation(summary = "Delete a community", description = "Deletes a community by its ID.")
    @ApiResponse(responseCode = "204", description = "Community deleted successfully")
    @ApiResponse(responseCode = "404", description = "Community not found")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommunity(@PathVariable String id) {
        communityService.deleteCommunity(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Join a community", description = "Allows a user to join a community.")
    @ApiResponse(responseCode = "200", description = "User joined successfully")
    @ApiResponse(responseCode = "404", description = "Community or user not found")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @PostMapping("/{communityId}/join/{userId}")
    public ResponseEntity<Community> joinCommunity(@PathVariable String communityId, @PathVariable String userId) {
        Community updatedCommunity = communityService.joinCommunity(communityId, userId);
        return ResponseEntity.ok(updatedCommunity);
    }
}
