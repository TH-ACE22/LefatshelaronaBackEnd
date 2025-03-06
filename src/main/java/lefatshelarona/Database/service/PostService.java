package lefatshelarona.Database.service;

import lefatshelarona.Database.model.Post;
import lefatshelarona.Database.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    
    private final PostRepository postRepository;

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Optional<Post> getPostById(String postId) {
        return postRepository.findById(postId);
    }

    public List<Post> getPostsByChannel(String channelId) {
        return postRepository.findByChannelId(channelId);
    }

    public List<Post> getPostsByUser(String username) {
        return postRepository.findByUsername(username);
    }

    public void deletePost(String postId) {
        postRepository.deleteById(postId);
    }

    public Post likePost(String postId, String userId) {
        return postRepository.findById(postId)
            .map(post -> {
                if (!post.getLikes().contains(userId)) {
                    post.getLikes().add(userId);
                    post.getDislikes().remove(userId);
                }
                return postRepository.save(post);
            })
            .orElseThrow(() -> new RuntimeException("Post not found!"));
    }

    public Post dislikePost(String postId, String userId) {
        return postRepository.findById(postId)
            .map(post -> {
                if (!post.getDislikes().contains(userId)) {
                    post.getDislikes().add(userId);
                    post.getLikes().remove(userId);
                }
                return postRepository.save(post);
            })
            .orElseThrow(() -> new RuntimeException("Post not found!"));
    }

    public Post addComment(String postId, String userId, String comment) {
        return postRepository.findById(postId)
            .map(post -> {
                post.getComments().add(Map.of(userId, comment));
                return postRepository.save(post);
            })
            .orElseThrow(() -> new RuntimeException("Post not found!"));
    }
}
