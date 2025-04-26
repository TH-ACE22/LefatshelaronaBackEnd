package lefatshelarona.Database.repository;

import lefatshelarona.Database.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByChannelId(String channelId);
    List<Notification> findByChannelIdIn(List<String> channelIds);
}
