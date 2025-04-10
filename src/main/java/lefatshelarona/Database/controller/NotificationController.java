package lefatshelarona.Database.controller;

import lefatshelarona.Database.model.Notification;
import lefatshelarona.Database.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@Tag(name = "Notification Management", description = "Operations related to notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Operation(summary = "Get notifications by channel", description = "Retrieves notifications for a specific channel.")
    @ApiResponse(responseCode = "200", description = "Notifications retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Notification.class)))
    @ApiResponse(responseCode = "404", description = "Channel not found")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @GetMapping("/{channelId}")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable String channelId) {
        return ResponseEntity.ok(notificationService.getNotificationsByChannel(channelId));
    }

    @Operation(summary = "Send a notification", description = "Sends a notification to a specified channel.")
    @ApiResponse(responseCode = "201", description = "Notification sent successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Notification.class)))
    @ApiResponse(responseCode = "400", description = "Invalid request body")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @PostMapping("/send")
    public ResponseEntity<Notification> sendNotification(@RequestBody Notification notification) {
        Notification sentNotification = notificationService.sendNotification(notification);
        return ResponseEntity.ok(sentNotification);
    }
}