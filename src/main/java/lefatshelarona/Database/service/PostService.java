package lefatshelarona.Database.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lefatshelarona.Database.model.Post;
import lefatshelarona.Database.model.Comment;
import lefatshelarona.Database.repository.PostRepository;
import lefatshelarona.Database.realtime.RealtimeService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final RealtimeService realtimeService;

    public Post createPost(Post post) {
        Post saved = postRepository.save(post);
        realtimeService.broadcastNewPost(saved, post.getChannelId());
        return saved;
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
        Post updated = postRepository.findById(postId)
                .map(post -> {
                    if (!post.getLikes().contains(userId)) {
                        post.getLikes().add(userId);
                        post.getDislikes().remove(userId);
                    }
                    return post;
                })
                .map(postRepository::save)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        realtimeService.broadcastPostLikeUpdate(updated.getId(), updated.getLikes().size());
        return updated;
    }

    public Post dislikePost(String postId, String userId) {
        Post updated = postRepository.findById(postId)
                .map(post -> {
                    if (!post.getDislikes().contains(userId)) {
                        post.getDislikes().add(userId);
                        post.getLikes().remove(userId);
                    }
                    return post;
                })
                .map(postRepository::save)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        realtimeService.broadcastPostLikeUpdate(updated.getId(), updated.getLikes().size());
        return updated;
    }

    public Post addComment(String postId, String userId, String username, String commentText) {
        Post updated = postRepository.findById(postId)
                .map(post -> {
                    Comment comment = new Comment();
                    comment.setUserId(userId);
                    comment.setUsername(username);
                    comment.setText(commentText); // 🛠 Corrected
                    post.getComments().add(comment);
                    return post;
                })
                .map(postRepository::save)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!updated.getComments().isEmpty()) {
            Comment latestComment = updated.getComments().get(updated.getComments().size() - 1);
            realtimeService.broadcastNewComment(latestComment, postId);
        }

        return updated;
    }

    public Post replyToComment(String postId, String commentId, String userId, String username, String replyText) {
        Post updated = postRepository.findById(postId)
                .map(post -> {
                    post.getComments().stream()
                            .filter(c -> c.getId().equals(commentId)) // 🛠 Corrected
                            .findFirst()
                            .ifPresent(parent -> {
                                Comment reply = new Comment();
                                reply.setUserId(userId);
                                reply.setUsername(username);
                                reply.setText(replyText); // 🛠 Corrected
                                parent.getReplies().add(reply);
                            });
                    return post;
                })
                .map(postRepository::save)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return updated;
    }
}
