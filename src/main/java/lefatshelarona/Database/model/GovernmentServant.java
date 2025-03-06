package lefatshelarona.Database.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Document(collection = "government_servants")
public class GovernmentServant {

    @Id
    private String governmentServantId;
    private String name;
    private String email;
    private String contactNumber;
    private String governmentDepartment;
    private String role;
    private String waterIssues;
    private List<String> assignedChannels;

    public GovernmentServant() {
        this.governmentServantId = UUID.randomUUID().toString(); // Generate unique ID
    }

    public GovernmentServant(String name, String email, String contactNumber, String governmentDepartment, String role, String waterIssues, List<String> assignedChannels) {
        this.governmentServantId = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
        this.governmentDepartment = governmentDepartment;
        this.role = role;
        this.waterIssues = waterIssues;
        this.assignedChannels = assignedChannels;
    }

    // Getters and Setters
    public String getGovernmentServantId() { return governmentServantId; }
    public void setGovernmentServantId(String governmentServantId) { this.governmentServantId = governmentServantId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getGovernmentDepartment() { return governmentDepartment; }
    public void setGovernmentDepartment(String governmentDepartment) { this.governmentDepartment = governmentDepartment; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getWaterIssues() { return waterIssues; }
    public void setWaterIssues(String waterIssues) { this.waterIssues = waterIssues; }

    public List<String> getAssignedChannels() { return assignedChannels; }
    public void setAssignedChannels(List<String> assignedChannels) { this.assignedChannels = assignedChannels; }
}
