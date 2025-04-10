package lefatshelarona.Database.model;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;
@Data
@Document(collection = "channels")

public class Channel {
    @Id
    private String id; // Let MongoDB generate the ID

    private String channelName;
    private String communityId;
    private List<String> users = new ArrayList<>();

    public void addUser(String userId) {
        if (!this.users.contains(userId)) {
            this.users.add(userId);
        }
    }

    public void removeUser(String userId) {
        this.users.remove(userId);
    }
}
