package lefatshelarona.Database.repository;

import lefatshelarona.Database.model.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QueryRepository extends MongoRepository<Query, String> {
    List<Query> findBySentBy(String sentBy);
    List<Query> findByChannelId(String channelId);
    List<Query> findByCommunityId(String communityId);
}
