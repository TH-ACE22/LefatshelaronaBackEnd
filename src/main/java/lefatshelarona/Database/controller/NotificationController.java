package lefatshelarona.Database.controller;

import lefatshelarona.Database.model.Notification;
import lefatshelarona.Database.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/{channelId}")
    public List<Notification> getNotifications(@PathVariable String channelId) {
        return notificationService.getNotificationsByChannel(channelId);
    }

    @PostMapping("/send")
    public ResponseEntity<Notification> sendNotification(@RequestBody Notification notification) {
        Notification sentNotification = notificationService.sendNotification(notification);
        return ResponseEntity.ok(sentNotification);
    }
}
