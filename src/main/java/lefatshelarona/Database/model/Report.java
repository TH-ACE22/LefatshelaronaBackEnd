package lefatshelarona.Database.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reports")
public class Report {
    @Id
    private String id;
    private String message;
    private List<String> attachments; // Cloudinary URLs
    private String sentBy;
    private String channelId;
    private Date submittedAt;
}
