package lefatshelarona.Database.repository;

import lefatshelarona.Database.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;  // <-- must be java.util.List

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByChannelId(String channelId);
    List<Post> findByUsername(String username);
}
