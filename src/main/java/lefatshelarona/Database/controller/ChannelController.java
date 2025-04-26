package lefatshelarona.Database.controller;

import lefatshelarona.Database.model.Channel;
import lefatshelarona.Database.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing channels.
 */
@RestController
@RequestMapping("/channels")
@Tag(name = "Channel Management", description = "Operations related to channel management")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    /**
     * Create a new channel.
     * @param channel the channel to create
     * @return the created channel with HTTP 201 status
     */
    @Operation(summary = "Create a new channel for a community")
    @PostMapping("/community/{communityId}")
    public ResponseEntity<Channel> createChannelForCommunity(
            @PathVariable String communityId,
            @RequestBody Channel channel
    ) {
        Channel created = channelService.createChannel(channel, communityId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    /**
     * Delete a channel by its ID.
     * @param id the ID of the channel to delete
     * @return HTTP 204 on successful deletion
     */
    @Operation(summary = "Delete a channel")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        channelService.deleteChannel(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Allow an authenticated user to join a channel.
     * @param channelId the ID of the channel
     * @param auth the JWT authentication token
     * @return the updated channel object
     */
    @Operation(summary = "Join a channel")
    @PostMapping("/{channelId}/join")
    public ResponseEntity<Channel> join(
            @PathVariable String channelId,
            JwtAuthenticationToken auth
    ) {
        String userId = auth.getToken().getSubject();
        Channel updated = channelService.joinChannel(channelId, userId);
        return ResponseEntity.ok(updated);
    }

    /**
     * Allow an authenticated user to leave a channel.
     * @param channelId the ID of the channel
     * @param auth the JWT authentication token
     * @return the updated channel object
     */
    @Operation(summary = "Leave a channel")
    @PostMapping("/{channelId}/leave")
    public ResponseEntity<Channel> leave(
            @PathVariable String channelId,
            JwtAuthenticationToken auth
    ) {
        String userId = auth.getToken().getSubject();
        Channel updated = channelService.leaveChannel(channelId, userId);
        return ResponseEntity.ok(updated);
    }

    /**
     * Fetch a channel by its ID.
     * @param id the channel ID
     * @return the channel if found, or 404 otherwise
     */
    @Operation(summary = "Get a channel by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Channel> getById(@PathVariable("id") String id) {
        return channelService.getChannelById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieve all channels.
     * @return list of all channels
     */
    @Operation(summary = "Get all channels")
    @GetMapping
    public ResponseEntity<List<Channel>> getAll() {
        List<Channel> channels = channelService.getAllChannels();
        return ResponseEntity.ok(channels);
    }
}
