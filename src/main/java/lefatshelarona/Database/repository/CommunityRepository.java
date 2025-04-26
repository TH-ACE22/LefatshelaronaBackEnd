package lefatshelarona.Database.repository;

import lefatshelarona.Database.model.Community;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Community entities.
 * Adds a custom query method to find all communities containing a specific member ID.
 */
public interface CommunityRepository extends MongoRepository<Community, String> {
    /**
     * Finds all communities that include the given userId in their members set.
     *
     * @param userId the ID of the user
     * @return list of communities the user belongs to
     */
    List<Community> findByMembersContaining(String userId);
    @NotNull
    Optional<Community> findById(@NotNull String id); // ✅ correct

}
