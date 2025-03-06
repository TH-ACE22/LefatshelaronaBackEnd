package lefatshelarona.Database.repository;

import lefatshelarona.Database.model.GovernmentService;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface GovernmentServiceRepository extends MongoRepository<GovernmentService, String> {
    Optional<GovernmentService> findByName(String name);
}
