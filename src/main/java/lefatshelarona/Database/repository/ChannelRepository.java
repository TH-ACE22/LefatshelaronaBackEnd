package lefatshelarona.Database.repository;

import lefatshelarona.Database.model.Channel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelRepository extends MongoRepository<Channel, String> {
    Optional<Channel> findByChannelName(String channelName);
    List<Channel> findByUsersContaining(String userId);
    List <Channel> findAllByCommunityId(String userId);

}
