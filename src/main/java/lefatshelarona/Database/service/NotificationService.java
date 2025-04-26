package lefatshelarona.Database.service;

import lefatshelarona.Database.model.Channel;
import lefatshelarona.Database.model.Notification;
import lefatshelarona.Database.repository.ChannelRepository;
import lefatshelarona.Database.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChannelRepository channelRepository;

    // ✅ Fetch all notifications for a specific channel
    public List<Notification> getNotificationsByChannel(String channelId) {
        return notificationRepository.findByChannelId(channelId);
    }

    // ✅ Fetch all notifications for the channels a user has joined
    public List<Notification> getNotificationsForUser(String userId) {
        List<Channel> joinedChannels = channelRepository.findByUsersContaining(userId);
        List<String> joinedChannelIds = joinedChannels.stream()
                .map(Channel::getId)
                .collect(Collectors.toList());

        return notificationRepository.findByChannelIdIn(joinedChannelIds);
    }

    // ✅ Send notification and broadcast only to joined users
    public Notification sendNotification(Notification notification) {
        Notification saved = notificationRepository.save(notification);

        // Broadcast only to joined users
        channelRepository.findById(notification.getChannelId()).ifPresent(channel -> {
            for (String userId : channel.getJoinedUsers()) {
                messagingTemplate.convertAndSend(
                        "/user/" + userId + "/queue/notifications",
                        saved
                );
            }
        });

        return saved;
    }
}
