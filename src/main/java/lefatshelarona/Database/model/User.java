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
    private String email;
    private String fullName;
    private List<String> joinedChannels;
    private String location;
    private String name;
    private String phone;
    private String profilePicture;
    private String role;
    private String username;
    private String password;  // Add this field for authentication

    public User() {}

    public User(String email, String fullName, List<String> joinedChannels, String location,
                String name, String phone, String profilePicture, String role, String username, String password) {
        this.email = email;
        this.fullName = fullName;
        this.joinedChannels = joinedChannels;
        this.location = location;
        this.name = name;
        this.phone = phone;
        this.profilePicture = profilePicture;
        this.role = role;
        this.username = username;
        this.password = password;
    }
}
