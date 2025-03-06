package lefatshelarona.Database.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    private String id;
    private String channelId;
    private String message;
    private String queryId;
    private String sentBy;

    @CreatedDate
    private Date sentAt = new Date();
}
