package lefatshelarona.Database.controller;

import lefatshelarona.Database.dto.CommunitySummaryDTO;
import lefatshelarona.Database.model.Community;
import lefatshelarona.Database.model.Channel;
import lefatshelarona.Database.service.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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
    @GetMapping
    public ResponseEntity<List<Community>> getAll() {
        return ResponseEntity.ok(communityService.getAllCommunities());
    }

    @Operation(summary = "Get a community by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Community> getById(@PathVariable String id) {
        return communityService.getCommunityById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new community")
    @PostMapping
    public ResponseEntity<Community> create(@RequestBody Community c) {
        return ResponseEntity.status(201).body(communityService.createCommunity(c));
    }

    @Operation(summary = "Update a community")
    @PutMapping("/{id}")
    public ResponseEntity<Community> update(@PathVariable String id, @RequestBody Community c) {
        return communityService.updateCommunity(id, c)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a community")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        communityService.deleteCommunity(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Join a community")
    @PostMapping("/{communityId}/join")
    public ResponseEntity<Community> join(@PathVariable String communityId, JwtAuthenticationToken authToken) {
        String userId = authToken.getToken().getSubject();
        return ResponseEntity.ok(communityService.joinCommunity(communityId, userId));
    }

    @Operation(summary = "Leave a community")
    @PostMapping("/{communityId}/leave")
    public ResponseEntity<Community> leave(@PathVariable String communityId, JwtAuthenticationToken authToken) {
        String userId = authToken.getToken().getSubject();
        return ResponseEntity.ok(communityService.leaveCommunity(communityId, userId));
    }

    @Operation(summary = "Get members of a community")
    @GetMapping("/{communityId}/members")
    public ResponseEntity<List<String>> members(@PathVariable String communityId) {
        return ResponseEntity.ok(communityService.getMembers(communityId));
    }

    @Operation(summary = "Get communities a user has joined")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Community>> userCommunities(@PathVariable String userId) {
        return ResponseEntity.ok(communityService.getCommunitiesForUser(userId));
    }

    // ✅ New Endpoint: Get full Channel objects in a community
    @Operation(summary = "Get full channel details for a community")
    @GetMapping("/{communityId}/channels")
    public ResponseEntity<List<Channel>> getChannels(@PathVariable String communityId) {
        return ResponseEntity.ok(communityService.getChannelsForCommunity(communityId));
    }
    @Operation(summary = "List all communities with member count and description")
    @GetMapping("/summaries")
    public ResponseEntity<List<CommunitySummaryDTO>> getCommunitySummaries() {
        return ResponseEntity.ok(communityService.getCommunitySummaries());
    }

    // ✅ New Endpoint: Get just channel IDs in a community
    @Operation(summary = "Get channel IDs in a community")
    @GetMapping("/{communityId}/channel-ids")
    public ResponseEntity<List<String>> getChannelIds(@PathVariable String communityId) {
        return ResponseEntity.ok(communityService.getChannelIds(communityId));
    }

    // ✅ New Endpoint: Add a channel to a community
    @Operation(summary = "Add a channel to a community by channelId")
    @PostMapping("/{communityId}/channels/{channelId}/add")
    public ResponseEntity<Community> addChannel(@PathVariable String communityId, @PathVariable String channelId) {
        return ResponseEntity.ok(communityService.addChannelToCommunity(communityId, channelId));
    }

    // ✅ New Endpoint: Remove a channel from a community
    @Operation(summary = "Remove a channel from a community by channelId")
    @DeleteMapping("/{communityId}/channels/{channelId}/remove")
    public ResponseEntity<Community> removeChannel(@PathVariable String communityId, @PathVariable String channelId) {
        return ResponseEntity.ok(communityService.removeChannelFromCommunity(communityId, channelId));
    }
}
