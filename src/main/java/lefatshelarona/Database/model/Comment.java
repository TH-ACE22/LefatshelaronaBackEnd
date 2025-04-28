package lefatshelarona.Database.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "comments")
public class Comment {

    @Id
    private String id;

    private String postId;      // Link to Post
    private String userId;
    private String username;
    private String text;
    private Date timestamp;

    @Builder.Default
    private List<Comment> replies = new ArrayList<>(); // Nested replies initialized
}
