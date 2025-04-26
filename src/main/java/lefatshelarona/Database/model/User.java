package lefatshelarona.Database.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Field("keycloak_id")
    @NotBlank
    private String keycloakId;

    @Field("email_verified")
    private boolean emailVerified;

    @Email
    @NotBlank
    @Indexed(unique = true)
    private String email;

    @NotBlank
    @Indexed(unique = true)
    private String username;

    @NotBlank
    @Field("first_name")
    private String firstName;

    @NotBlank
    @Field("last_name")
    private String lastName;

    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Invalid phone number")
    private String phone;

    @NotBlank
    private String role;

    @Field("profile_picture")
    private String profilePicture;       // Cloudinary URL

    private String community;            // Community ID or name

    @Builder.Default
    @Field("joined_channels")
    private Set<String> joinedChannels = new HashSet<>();
}
