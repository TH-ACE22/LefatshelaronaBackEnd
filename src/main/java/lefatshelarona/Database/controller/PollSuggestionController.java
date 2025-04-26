package lefatshelarona.Database.controller;

import lefatshelarona.Database.dto.SuggestionRankUpdateRequest;
import lefatshelarona.Database.model.PollSuggestion;
import lefatshelarona.Database.service.PollSuggestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/polls")
@RequiredArgsConstructor
@Tag(name = "Poll Suggestions", description = "Submit and fetch poll suggestions by community")
public class PollSuggestionController {

    private final PollSuggestionService pollSuggestionService;

    @Operation(
            summary = "Submit a new poll suggestion",
            description = "Users can suggest new poll topics for a specific community",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Suggestion submitted successfully",
                            content = @Content(schema = @Schema(implementation = PollSuggestion.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    @PostMapping("/suggest")
    public ResponseEntity<PollSuggestion> submitPollSuggestion(
            @RequestParam String text,
            @RequestParam String communityId,
            JwtAuthenticationToken auth
    ) {
        String userId = auth.getToken().getSubject();
        PollSuggestion suggestion = pollSuggestionService.saveSuggestion(text, userId, communityId);
        return ResponseEntity.ok(suggestion);
    }

    @Operation(
            summary = "Get all poll suggestions",
            description = "Retrieves all poll suggestions regardless of community"
    )
    @GetMapping("/suggestions")
    public ResponseEntity<List<PollSuggestion>> getAllSuggestions() {
        return ResponseEntity.ok(pollSuggestionService.getAllSuggestions());
    }

    @Operation(
            summary = "Get poll suggestions by community ID",
            description = "Fetches all poll suggestions submitted under a specific community",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "List of poll suggestions",
                    content = @Content(schema = @Schema(implementation = PollSuggestion.class))
            )
    )
    @GetMapping("/suggestions/community/{communityId}")
    public ResponseEntity<List<PollSuggestion>> getSuggestionsByCommunity(@PathVariable String communityId) {
        return ResponseEntity.ok(pollSuggestionService.getSuggestionsByCommunity(communityId));
    }
    @PatchMapping("/suggestions/rank")
    public ResponseEntity<PollSuggestion> updateSuggestionRank(
            @RequestBody SuggestionRankUpdateRequest request) {
        PollSuggestion updated = pollSuggestionService.updateSuggestionRank(
                request.getSuggestionId(), request.getNewRank());
        return ResponseEntity.ok(updated);
    }

}
