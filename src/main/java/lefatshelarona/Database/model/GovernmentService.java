package lefatshelarona.Database.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "government_services")
public class GovernmentService {
    @Id
    private String id;
    private String name;
    private String channelId;
    private String description;
    private String contactEmail;

    public GovernmentService() {
    }

    public GovernmentService(String name, String channelId, String description, String contactEmail) {
        this.name = name;
        this.channelId = channelId;
        this.description = description;
        this.contactEmail = contactEmail;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}
