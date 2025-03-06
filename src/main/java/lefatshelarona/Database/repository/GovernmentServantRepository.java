package lefatshelarona.Database.repository;

import lefatshelarona.Database.model.GovernmentServant;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface GovernmentServantRepository extends MongoRepository<GovernmentServant, String> {
    Optional<GovernmentServant> findByEmail(String email);
}
