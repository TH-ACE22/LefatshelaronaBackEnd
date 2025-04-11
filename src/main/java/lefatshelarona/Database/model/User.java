package lefatshelarona.Database.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

import java.util.List;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;

    private String keycloakId;
    private boolean emailVerified;

    private String email;
    private String fullName;
    private List<String> joinedChannels;
    private String community;                // 🔧 New field added here
    private String name;
    private String phone;
    private String profilePicture;
    private String username;
    private String password;
    private String role;

    public User() {}

    public User(String email, String fullName, List<String> joinedChannels, String community,
                String name, String phone, String profilePicture, String role,
                String username, String password, String keycloakId, boolean emailVerified) {
        this.email = email;
        this.fullName = fullName;
        this.joinedChannels = joinedChannels;
        this.community = community;           // 👈 Assigning the community
        this.name = name;
        this.phone = phone;
        this.profilePicture = profilePicture;
        this.role = role;
        this.username = username;
        this.password = password;
        this.keycloakId = keycloakId;
        this.emailVerified = emailVerified;
    }
}
