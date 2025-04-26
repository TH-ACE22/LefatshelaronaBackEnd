// Comment.java
package lefatshelarona.Database.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Comment {
    @Id
    private String commentId;
    private String userId;
    private String username;
    private String comment;
    private Date timestamp = new Date();
    private List<String> likes = new ArrayList<>();
    private List<Comment> replies = new ArrayList<>();
}
