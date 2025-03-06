package lefatshelarona.Database.repository;

import lefatshelarona.Database.model.Community;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommunityRepository extends MongoRepository<Community, String> {
}
