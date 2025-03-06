package lefatshelarona.Database.service;

import lefatshelarona.Database.model.Notification;
import lefatshelarona.Database.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getNotificationsByChannel(String channelId) {
        return notificationRepository.findByChannelId(channelId);
    }

    public Notification sendNotification(Notification notification) {
        return notificationRepository.save(notification);
    }
}
