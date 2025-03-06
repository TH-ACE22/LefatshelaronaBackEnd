package lefatshelarona.Database.controller;

import lefatshelarona.Database.model.Post;
import lefatshelarona.Database.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('USER')") // Ensure only authenticated users create posts
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        return ResponseEntity.ok(postService.createPost(post));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable String postId) {
        Optional<Post> post = postService.getPostById(postId);
        return post.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<Post>> getPostsByChannel(@PathVariable String channelId) {
        return ResponseEntity.ok(postService.getPostsByChannel(channelId));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<Post>> getPostsByUser(@PathVariable String username) {
        return ResponseEntity.ok(postService.getPostsByUser(username));
    }

    @DeleteMapping("/delete/{postId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')") // Ensures only the creator or an admin can delete
    public ResponseEntity<Void> deletePost(@PathVariable String postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Post> likePost(@PathVariable String postId, @RequestParam String userId) {
        return ResponseEntity.ok(postService.likePost(postId, userId));
    }

    @PostMapping("/{postId}/dislike")
    public ResponseEntity<Post> dislikePost(@PathVariable String postId, @RequestParam String userId) {
        return ResponseEntity.ok(postService.dislikePost(postId, userId));
    }

    @PostMapping("/{postId}/comment")
    public ResponseEntity<Post> addComment(@PathVariable String postId, @RequestParam String userId, @RequestParam String comment) {
        return ResponseEntity.ok(postService.addComment(postId, userId, comment));
    }
}
