package lefatshelarona.Database.service;

import lefatshelarona.Database.dto.LoginRequest;
import lefatshelarona.Database.dto.RegisterRequest;
import lefatshelarona.Database.repository.UserRepository;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import jakarta.ws.rs.core.Response;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final Keycloak keycloak;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    public AuthService(Keycloak keycloak, UserRepository userRepository) {
        this.keycloak = keycloak;
        this.userRepository = userRepository;
    }

    public boolean isUsernameAvailable(String username) {
        return userRepository.findByUsername(username).isEmpty();
    }

    public boolean isEmailAvailable(String email) {
        try {
            String decodedEmail = URLDecoder.decode(email, StandardCharsets.UTF_8);
            UsersResource usersResource = keycloak.realm("Lefatshe-Larona").users();
            return usersResource.search(null, null, null, decodedEmail, 0, 1).isEmpty();
        } catch (Exception e) {
            return false; // Assume email not available on error
        }
    }

    public ResponseEntity<?> registerUser(RegisterRequest request) {
        Map<String, String> errorDetails = new HashMap<>();

        if (!isEmailAvailable(request.getEmail())) {
            errorDetails.put("email", "Email already exists.");
        }

        if (!errorDetails.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDetails);
        }

        UsersResource usersResource = keycloak.realm("Lefatshe-Larona").users();

        UserRepresentation keycloakUser = new UserRepresentation();
        keycloakUser.setUsername(request.getUsername());
        keycloakUser.setEmail(request.getEmail());
        keycloakUser.setFirstName(request.getFullName());
        keycloakUser.setEnabled(true);
        keycloakUser.setEmailVerified(false);
        keycloakUser.setRequiredActions(Collections.singletonList("VERIFY_EMAIL"));

        CredentialRepresentation password = new CredentialRepresentation();
        password.setType(CredentialRepresentation.PASSWORD);
        password.setValue(request.getPassword());
        password.setTemporary(false);
        keycloakUser.setCredentials(Collections.singletonList(password));

        try (Response response = usersResource.create(keycloakUser)) {
            if (response.getStatus() == 201) {
                // ✅ BONUS: Trigger the verification email after account creation
                String locationHeader = response.getHeaderString("Location"); // e.g. .../users/{userId}
                String userId = locationHeader != null ? locationHeader.substring(locationHeader.lastIndexOf("/") + 1) : null;

                if (userId != null) {
                    try {
                        usersResource.get(userId).sendVerifyEmail();
                    } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(Map.of("error", "User created, but failed to send verification email."));
                    }
                }

                return ResponseEntity.ok("Account created. Please check your email to verify.");
            } else {
                Map<String, String> keycloakError = new HashMap<>();
                keycloakError.put("error", "Failed to register user in Keycloak.");
                return ResponseEntity.status(response.getStatus()).body(keycloakError);
            }
        }
    }




    // Optional endpoint you can create: POST /auth/save-profile-after-verification
    // That endpoint would take keycloakId or email and save the verified user info into MongoDB

    public ResponseEntity<String> loginUser(LoginRequest request) {
        String tokenUrl =
                "http://localhost:8080/realms/Lefatshe-Larona/protocol/openid-connect/token";

        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", "your-client-id");
        formData.add("client_secret", "your-client-secret");
        formData.add("grant_type", "password");
        formData.add("username", request.getUsername());
        formData.add("password", request.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity =
                new HttpEntity<>(formData, headers);

        try {
            ResponseEntity<String> response =
                    restTemplate.postForEntity(tokenUrl, entity, String.class);
            return ResponseEntity.status(response.getStatusCode())
                    .body(response.getBody());
        } catch (Exception e) {
            logger.error("Login failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Login error: " + e.getMessage());
        }
    }
}
