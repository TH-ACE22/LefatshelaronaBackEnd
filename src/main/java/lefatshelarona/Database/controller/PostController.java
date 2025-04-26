package lefatshelarona.Database.controller;

import lefatshelarona.Database.model.Post;
import  lefatshelarona.Database.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
@Tag(name = "Post Management", description = "Operations related to user posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "Create a new post", description = "Allows a user to create a new post.")
    @ApiResponse(responseCode = "201", description = "Post created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class)))
    @ApiResponse(responseCode = "400", description = "Invalid request body")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        Post created = postService.createPost(post);
        return ResponseEntity.status(201).body(created);
    }

    @Operation(summary = "Get a post by ID", description = "Fetches details of a specific post.")
    @ApiResponse(responseCode = "200", description = "Post found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class)))
    @ApiResponse(responseCode = "404", description = "Post not found")
    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable String postId) {
        Optional<Post> post = postService.getPostById(postId);
        return post.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get posts by channel", description = "Retrieves all posts for a specific channel.")
    @ApiResponse(responseCode = "200", description = "Posts retrieved successfully")
    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<Post>> getPostsByChannel(@PathVariable String channelId) {
        List<Post> posts = postService.getPostsByChannel(channelId);
        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "Get posts by user", description = "Retrieves all posts made by a specific user.")
    @ApiResponse(responseCode = "200", description = "Posts retrieved successfully")
    @GetMapping("/user/{username}")
    public ResponseEntity<List<Post>> getPostsByUser(@PathVariable String username) {
        List<Post> posts = postService.getPostsByUser(username);
        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "Delete a post", description = "Allows an admin or the post creator to delete a post.")
    @ApiResponse(responseCode = "204", description = "Post deleted successfully")
    @ApiResponse(responseCode = "404", description = "Post not found")
    @ApiResponse(responseCode = "403", description = "Forbidden - Unauthorized access")
    @DeleteMapping("/delete/{postId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Void> deletePost(@PathVariable String postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Like a post", description = "Allows a user to like a post.")
    @ApiResponse(responseCode = "200", description = "Post liked successfully")
    @PostMapping("/{postId}/like")
    public ResponseEntity<Post> likePost(
            @PathVariable String postId,
            @RequestParam String userId
    ) {
        Post updated = postService.likePost(postId, userId);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Dislike a post", description = "Allows a user to dislike a post.")
    @ApiResponse(responseCode = "200", description = "Post disliked successfully")
    @PostMapping("/{postId}/dislike")
    public ResponseEntity<Post> dislikePost(
            @PathVariable String postId,
            @RequestParam String userId
    ) {
        Post updated = postService.dislikePost(postId, userId);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Comment on a post", description = "Allows a user to add a comment to a post.")
    @ApiResponse(responseCode = "200", description = "Comment added successfully")
    @PostMapping("/{postId}/comment")
    public ResponseEntity<Post> addComment(
            @PathVariable String postId,
            @RequestParam String userId,
            @RequestParam String username,
            @RequestParam String text
    ) {
        Post updated = postService.addComment(postId, userId, username, text);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Reply to a comment", description = "Allows a user to reply to an existing comment.")
    @ApiResponse(responseCode = "200", description = "Reply added successfully")
    @PostMapping("/{postId}/comment/{commentId}/reply")
    public ResponseEntity<Post> replyToComment(
            @PathVariable String postId,
            @PathVariable String commentId,
            @RequestParam String userId,
            @RequestParam String username,
            @RequestParam String text
    ) {
        Post updated = postService.replyToComment(postId, commentId, userId, username, text);
        return ResponseEntity.ok(updated);
    }
}
