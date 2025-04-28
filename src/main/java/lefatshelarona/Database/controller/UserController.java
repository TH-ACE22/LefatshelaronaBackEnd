package lefatshelarona.Database.controller;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lefatshelarona.Database.model.User;
import lefatshelarona.Database.repository.UserRepository;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "Operations related to user management in the Lefatshe-Larona realm")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Keycloak keycloak;

    @Operation(summary = "Create a new user", description = "Adds a new user to the local database.")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        return ResponseEntity.status(201).body(savedUser);
    }

    @Operation(summary = "Get all users", description = "Fetches all users from the system.")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @Operation(summary = "Get user by ID", description = "Fetches a user by their ID.")
    @ApiResponse(responseCode = "200", description = "User retrieved successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/getById/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @Operation(summary = "Update user by ID", description = "Updates a user's information.")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setEmail(updatedUser.getEmail());
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setUsername(updatedUser.getUsername());
            user.setPhone(updatedUser.getPhone());
            user.setCommunity(updatedUser.getCommunity());
            user.setProfilePicture(updatedUser.getProfilePicture());
            user.setJoinedChannels(updatedUser.getJoinedChannels());
            // Optionally update role or other fields here

            return ResponseEntity.ok(userRepository.save(user));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete user by ID", description = "Deletes a user by their ID.")
    @ApiResponse(responseCode = "200", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
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
            description = "Triggers Keycloak to send a verification email to the user in the Lefatshe-Larona realm."
    )
    @ApiResponse(responseCode = "200", description = "Verification email sent successfully")
    @ApiResponse(responseCode = "500", description = "Error sending email")
    @PostMapping("/resendVerification/{keycloakUserId}")
    public ResponseEntity<String> resendVerificationEmail(@PathVariable String keycloakUserId) {
        try {
            UsersResource usersResource = keycloak.realm("Lefatshe-Larona").users();
            usersResource.get(keycloakUserId)
                    .executeActionsEmail(Collections.singletonList("VERIFY_EMAIL"));
            return ResponseEntity.ok("Verification email sent successfully to user in Lefatshe-Larona realm.");
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error sending verification email: " + e.getMessage());
        }
    }

    @Operation(
            summary = "View user by Mongo ID",
            description = "Returns the user document for the given MongoDB _id."
    )
    @ApiResponse(responseCode = "200", description = "User retrieved successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/view-user/{id}")
    public ResponseEntity<User> viewUserByMongoId(@PathVariable("id") String id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
