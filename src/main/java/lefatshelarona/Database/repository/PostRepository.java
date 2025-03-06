package lefatshelarona.Database.repository;

import lefatshelarona.Database.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByChannelId(String channelId);
    List<Post> findByUsername(String username);
}
