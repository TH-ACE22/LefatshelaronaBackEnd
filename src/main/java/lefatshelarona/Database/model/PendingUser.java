package lefatshelarona.Database.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "pending_users")
public class PendingUser {
    @Id
    private String keycloakId;
    private String username;
    private String email;
    // Instead of a single fullName field, use firstName and lastName:
    private String firstName;
    private String lastName;
    private String phone;
    private String role;
    private LocalDateTime createdAt;
}
