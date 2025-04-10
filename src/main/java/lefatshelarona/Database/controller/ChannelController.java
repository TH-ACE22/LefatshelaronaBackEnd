package lefatshelarona.Database.controller;

import lefatshelarona.Database.model.Channel;
import lefatshelarona.Database.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/channels")
@Tag(name = "Channel Management", description = "Operations related to channel management")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @Operation(summary = "Create a new channel", description = "Adds a new channel to the system.")
    @ApiResponse(responseCode = "201", description = "Channel created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Channel.class)))
    @ApiResponse(responseCode = "400", description = "Invalid request body")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @PostMapping("/create")
    public ResponseEntity<Channel> createChannel(@RequestBody Channel channel) {
        return ResponseEntity.ok(channelService.createChannel(channel));
    }

    @Operation(summary = "Delete a channel", description = "Deletes a channel by its ID.")
    @ApiResponse(responseCode = "204", description = "Channel deleted successfully")
    @ApiResponse(responseCode = "404", description = "Channel not found")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @DeleteMapping("/delete/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable String channelId) {
        channelService.deleteChannel(channelId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Join a channel", description = "Allows a user to join a channel.")
    @ApiResponse(responseCode = "200", description = "User joined successfully")
    @ApiResponse(responseCode = "404", description = "Channel or user not found")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @PostMapping("/join")
    public ResponseEntity<Channel> joinChannel(@RequestParam String channelId, @RequestParam String userId) {
        return ResponseEntity.ok(channelService.joinChannel(channelId, userId));
    }

    @Operation(summary = "Leave a channel", description = "Allows a user to leave a channel.")
    @ApiResponse(responseCode = "200", description = "User left the channel successfully")
    @ApiResponse(responseCode = "404", description = "Channel or user not found")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @PostMapping("/leave")
    public ResponseEntity<Channel> leaveChannel(@RequestParam String channelId, @RequestParam String userId) {
        return ResponseEntity.ok(channelService.leaveChannel(channelId, userId));
    }

    @Operation(summary = "Get a channel by ID", description = "Fetches details of a specific channel.")
    @ApiResponse(responseCode = "200", description = "Channel found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Channel.class)))
    @ApiResponse(responseCode = "404", description = "Channel not found")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @GetMapping("/{channelId}")
    public ResponseEntity<Channel> getChannelById(@PathVariable String channelId) {
        Optional<Channel> channel = channelService.getChannelById(channelId);
        return channel.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all channels", description = "Retrieves all available channels.")
    @ApiResponse(responseCode = "200", description = "Channels retrieved successfully")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @GetMapping("/all")
    public ResponseEntity<List<Channel>> getAllChannels() {
        return ResponseEntity.ok(channelService.getAllChannels());
    }
}
