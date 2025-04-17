package lefatshelarona.Database.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String keycloakId;
    private boolean emailVerified;

    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String username;
    private String role;

    private String profilePicture;       // to be filled later
    private String community;            // to be filled later
    private List<String> joinedChannels; // to be filled later

    public User(String email, String firstName, String lastName, String community,
                String phone, String profilePicture, String role,
                String username, String keycloakId, boolean emailVerified, List<String> joinedChannels) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.community = community;
        this.phone = phone;
        this.profilePicture = profilePicture;
        this.role = role;
        this.username = username;
        this.keycloakId = keycloakId;
        this.emailVerified = emailVerified;
        this.joinedChannels = joinedChannels;
    }
}
