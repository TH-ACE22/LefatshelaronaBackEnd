package lefatshelarona.Database.controller;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import jakarta.ws.rs.core.Response; // Using jakarta.ws.rs if using Keycloak admin client 22.x
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lefatshelarona.Database.model.User;
import lefatshelarona.Database.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "Operations related to user management in the Lefatshe-Larona realm")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Inject the Keycloak admin client (configured to use your Lefatshe-Larona realm)
    @Autowired
    private Keycloak keycloak;

    @Operation(summary = "Create a new user", description = "Adds a new user to the local database.")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userRepository.save(user));
    }

    @Operation(summary = "Get all users", description = "Fetches all users from the system.")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @Operation(summary = "Get user by ID", description = "Fetches a user by their ID.")
    @GetMapping("/getById/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update user by ID", description = "Updates a user's information.")
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setEmail(updatedUser.getEmail());
            user.setFullName(updatedUser.getFullName());
            user.setJoinedChannels(updatedUser.getJoinedChannels());
            user.setLocation(updatedUser.getLocation());
            user.setName(updatedUser.getName());
            user.setPhone(updatedUser.getPhone());
            user.setProfilePicture(updatedUser.getProfilePicture());
            // Optionally, if roles are solely managed by Keycloak, omit updating role here:
            // user.setRole(updatedUser.getRole());
            user.setUsername(updatedUser.getUsername());
            return ResponseEntity.ok(userRepository.save(user));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete user by ID", description = "Deletes a user by their ID.")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok("User deleted successfully.");
        }
        return ResponseEntity.status(404).body("User not found.");
    }

    @Operation(
            summary = "Resend Email Verification",
            description = "Triggers Keycloak to send a verification email to the user in the Lefatshe-Larona realm. " +
                    "Provide the Keycloak user ID (UID) as the path variable."
    )
    @ApiResponse(responseCode = "204", description = "Verification email sent successfully")
    @ApiResponse(responseCode = "400", description = "Failed to send verification email")
    @PostMapping("/resendVerification/{keycloakUserId}")
    public ResponseEntity<String> resendVerificationEmail(@PathVariable String keycloakUserId) {
        try {
            // Obtain the UsersResource for the Lefatshe-Larona realm
            UsersResource usersResource = keycloak.realm("Lefatshe-Larona").users();

            // Trigger the email verification action ("VERIFY_EMAIL")
            // Note: executeActionsEmail now returns void.
            usersResource.get(keycloakUserId).executeActionsEmail(Collections.singletonList("VERIFY_EMAIL"));

            // If no exception was thrown, assume success.
            return ResponseEntity.ok("Verification email sent successfully to user in Lefatshe-Larona realm.");
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error sending verification email: " + e.getMessage());
        }
    }
}
