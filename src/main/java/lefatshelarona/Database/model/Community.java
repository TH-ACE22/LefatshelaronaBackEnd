package lefatshelarona.Database.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "communities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Community {
    @Id
    private String id;
    private String name;
    private String location;
    private String description;

    private Set<String> channelIds = new HashSet<>();
    private Set<String> members = new HashSet<>();

    @CreatedDate
    private Date createdAt = new Date();

    // Member management
    public void addMember(String userId) {
        this.members.add(userId);
    }

    public void removeMember(String userId) {
        this.members.remove(userId);
    }

    // Channel management
    public void addChannelId(String channelId) {
        this.channelIds.add(channelId);
    }

    public void removeChannelId(String channelId) {
        this.channelIds.remove(channelId);
    }
}
