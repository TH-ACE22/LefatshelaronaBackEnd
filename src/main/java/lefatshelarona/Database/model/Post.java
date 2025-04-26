// Post.java
package lefatshelarona.Database.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "posts")
public class Post {
    @Id
    private String postId;
    private String username;
    private String name;
    private String channelId;
    private String content;
    private List<String> imageUrls;  // Save post image Cloudinary URLs
    private Date createdAt = new Date();
    private List<String> likes = new ArrayList<>();
    private List<String> dislikes = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();
}
