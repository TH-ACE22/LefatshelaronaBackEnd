package lefatshelarona.Database.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "posts")
public class Post {

    @Id
    private String id; // <---- this is required

    private String channelId;
    private String userId;
    private String username;
    private String content;
    private String imageUrl;
    private Date timestamp;

    @Builder.Default
    private List<String> likes = new java.util.ArrayList<>();
    @Builder.Default
    private List<String> dislikes = new java.util.ArrayList<>();
    @Builder.Default
    private List<Comment> comments = new java.util.ArrayList<>();
}
