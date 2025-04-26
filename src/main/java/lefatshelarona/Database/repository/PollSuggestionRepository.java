package lefatshelarona.Database.repository;

import lefatshelarona.Database.model.PollSuggestion;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PollSuggestionRepository extends MongoRepository<PollSuggestion, String> {
    List<PollSuggestion> findByChannelId(String channelId);
    List<PollSuggestion> findByCommunityId(String communityId);
    @NotNull
    Optional<PollSuggestion> findById(@NotNull String id);

}
