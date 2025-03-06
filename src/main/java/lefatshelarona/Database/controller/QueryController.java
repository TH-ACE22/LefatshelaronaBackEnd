package lefatshelarona.Database.controller;
import lefatshelarona.Database.model.Query;
import lefatshelarona.Database.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/queries")
public class QueryController {

    @Autowired
    private QueryService queryService;

    // Create a new query
    @PostMapping("/create")
    public ResponseEntity<Query> createQuery(@RequestBody Query query) {
        Query savedQuery = queryService.createQuery(query);
        return ResponseEntity.ok(savedQuery);
    }

    // Get all queries
    @GetMapping("/all")
    public ResponseEntity<List<Query>> getAllQueries() {
        return ResponseEntity.ok(queryService.getAllQueries());
    }

    // Get query by ID
    @GetMapping("/{queryId}")
    public ResponseEntity<Query> getQueryById(@PathVariable String queryId) {
        Optional<Query> query = queryService.getQueryById(queryId);
        return query.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get queries by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Query>> getQueriesByUser(@PathVariable String userId) {
        return ResponseEntity.ok(queryService.getQueriesByUser(userId));
    }

    // Get queries by community
    @GetMapping("/community/{communityId}")
    public ResponseEntity<List<Query>> getQueriesByCommunity(@PathVariable String communityId) {
        return ResponseEntity.ok(queryService.getQueriesByCommunity(communityId));
    }

    // Update query status
    @PatchMapping("/update/{queryId}")
    public ResponseEntity<Query> updateQueryStatus(@PathVariable String queryId, @RequestParam String status) {
        Query updatedQuery = queryService.updateQueryStatus(queryId, status);
        if (updatedQuery != null) {
            return ResponseEntity.ok(updatedQuery);
        }
        return ResponseEntity.notFound().build();
    }

    // Delete a query
    @DeleteMapping("/delete/{queryId}")
    public ResponseEntity<Void> deleteQuery(@PathVariable String queryId) {
        queryService.deleteQuery(queryId);
        return ResponseEntity.noContent().build();
    }
}
