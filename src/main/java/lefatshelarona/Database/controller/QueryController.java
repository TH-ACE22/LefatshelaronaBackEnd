package lefatshelarona.Database.controller;

import lefatshelarona.Database.model.Query;
import lefatshelarona.Database.service.QueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/queries")
@Tag(name = "Query Management", description = "Operations related to user queries")
public class QueryController {

    @Autowired
    private QueryService queryService;

    @Operation(summary = "Create a query", description = "Submits a new query.")
    @ApiResponse(responseCode = "201", description = "Query created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Query.class)))
    @ApiResponse(responseCode = "400", description = "Invalid query details")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @PostMapping("/create")
    public ResponseEntity<Query> createQuery(@RequestBody Query query) {
        Query savedQuery = queryService.createQuery(query);
        return ResponseEntity.ok(savedQuery);
    }

    @Operation(summary = "Get all queries", description = "Retrieves all queries.")
    @ApiResponse(responseCode = "200", description = "Queries retrieved successfully")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @GetMapping("/all")
    public ResponseEntity<List<Query>> getAllQueries() {
        return ResponseEntity.ok(queryService.getAllQueries());
    }

    @Operation(summary = "Get a query by ID", description = "Fetches details of a specific query.")
    @ApiResponse(responseCode = "200", description = "Query found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Query.class)))
    @ApiResponse(responseCode = "404", description = "Query not found")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @GetMapping("/{queryId}")
    public ResponseEntity<Query> getQueryById(@PathVariable String queryId) {
        Optional<Query> query = queryService.getQueryById(queryId);
        return query.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get queries by user", description = "Retrieves all queries submitted by a specific user.")
    @ApiResponse(responseCode = "200", description = "Queries retrieved successfully")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Query>> getQueriesByUser(@PathVariable String userId) {
        return ResponseEntity.ok(queryService.getQueriesByUser(userId));
    }

    @Operation(summary = "Get queries by community", description = "Retrieves all queries related to a specific community.")
    @ApiResponse(responseCode = "200", description = "Queries retrieved successfully")
    @GetMapping("/community/{communityId}")
    public ResponseEntity<List<Query>> getQueriesByCommunity(@PathVariable String communityId) {
        return ResponseEntity.ok(queryService.getQueriesByCommunity(communityId));
    }

    @Operation(summary = "Update query status", description = "Updates the status of a query.")
    @ApiResponse(responseCode = "200", description = "Query status updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Query.class)))
    @ApiResponse(responseCode = "404", description = "Query not found")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @PatchMapping("/update/{queryId}")
    public ResponseEntity<Query> updateQueryStatus(@PathVariable String queryId, @RequestParam String status) {
        Query updatedQuery = queryService.updateQueryStatus(queryId, status);
        if (updatedQuery != null) {
            return ResponseEntity.ok(updatedQuery);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete a query", description = "Deletes a query by its ID.")
    @ApiResponse(responseCode = "204", description = "Query deleted successfully")
    @ApiResponse(responseCode = "404", description = "Query not found")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @DeleteMapping("/delete/{queryId}")
    public ResponseEntity<Void> deleteQuery(@PathVariable String queryId) {
        queryService.deleteQuery(queryId);
        return ResponseEntity.noContent().build();
    }
}
