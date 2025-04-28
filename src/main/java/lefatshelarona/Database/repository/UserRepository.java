package lefatshelarona.Database.repository;

import lefatshelarona.Database.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    //— existing methods —
    boolean existsByUsernameIgnoreCase(String username);
    boolean existsByEmailIgnoreCase(String email);
    Optional<User> findByKeycloakId(String keycloakId);
    // this will do exactly the same as findById(...)
    default Optional<User> findByMongoId(String id) {
        return findById(id);
    }
    //— new methods —

    /**
     * Lookup a user document by their application username.
     */
    Optional<User> findByUsernameIgnoreCase(String username);

    /**
     * Lookup a user document by their email.
     */
    Optional<User> findByEmailIgnoreCase(String email);

    /**
     * Fetch all users with the given role (e.g. "ROLE_USER", "ROLE_ADMIN").
     */
    List<User> findByRole(String role);
}
