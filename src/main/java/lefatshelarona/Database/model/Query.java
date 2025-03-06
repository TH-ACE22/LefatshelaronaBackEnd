package lefatshelarona.Database.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "queries")  // Specifies the collection in MongoDB
public class Query {

    @Id
    private String queryId;       // Unique identifier for the query
    private String channelId;     // Channel associated with the query
    private String message;       // Message content
    private String status;        // Status of the query (e.g., "pending", "resolved")
    private Date submittedAt;     // Timestamp when query was submitted
    private String sentBy;        // The user ID (JWT-authenticated user)
    private String communityId;   // The community the query belongs to

    public Query() {
    }

    public Query(String queryId, String channelId, String message, String status, Date submittedAt, String sentBy, String communityId) {
        this.queryId = queryId;
        this.channelId = channelId;
        this.message = message;
        this.status = status;
        this.submittedAt = submittedAt;
        this.sentBy = sentBy;
        this.communityId = communityId;
    }

    // Getters and Setters
    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Date submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
}
