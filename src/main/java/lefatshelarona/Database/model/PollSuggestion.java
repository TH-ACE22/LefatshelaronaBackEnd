package lefatshelarona.Database.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Builder
@Data
@Document(collection = "poll_suggestions")
@NoArgsConstructor
@AllArgsConstructor
public class PollSuggestion {
    @Id
    private String id;
    private String communityId;
    private String channelId;
    private String userId;
    private String suggestionText;
    private int rank; // New field
    private Date submittedAt;
}
