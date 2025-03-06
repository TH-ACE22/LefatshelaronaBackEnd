package lefatshelarona.Database.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "posts") // MongoDB collection name
public class Post {
    @Id
    private String postId; // MongoDB auto-generated ID
    private String username; // The user who created the post
    private String name; // User's full name
    private String channelId; // The channel the post belongs to
    private String content; // Post content
    private Date createdAt = new Date(); // Timestamp of creation
    private List<String> likes; // List of user IDs who liked the post
    private List<String> dislikes; // List of user IDs who disliked the post
    private List<Map<String, String>> comments; // List of comments (userId -> comment text)
}
