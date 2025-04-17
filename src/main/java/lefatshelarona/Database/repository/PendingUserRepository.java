package lefatshelarona.Database.repository;

import lefatshelarona.Database.model.PendingUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface PendingUserRepository extends MongoRepository<PendingUser, String> {

    // Check if a username exists (case-insensitive)
    boolean existsByUsernameIgnoreCase(String username);

    // Check if an email exists (case-insensitive)
    boolean existsByEmailIgnoreCase(String email);

    // Retrieve a PendingUser by username in a case-insensitive manner
    Optional<PendingUser> findByUsernameIgnoreCase(String username);

    // Optional: Retrieve a PendingUser by email in a case-insensitive manner
    Optional<PendingUser> findByEmailIgnoreCase(String email);
}
