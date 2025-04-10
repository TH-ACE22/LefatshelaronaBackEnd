package lefatshelarona.Database.service;

import lefatshelarona.Database.dto.LoginRequest;
import lefatshelarona.Database.dto.RegisterRequest;
import lefatshelarona.Database.util.KeycloakUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import jakarta.ws.rs.core.Response;
import java.util.Collections;

@Service
public class AuthService {
    private final Keycloak keycloak;

    // Inject Keycloak via constructor
    public AuthService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public ResponseEntity<String> registerUser(RegisterRequest request) {
        UsersResource usersResource = keycloak.realm("Lefatshe-Larona").users();

        // Check if user already exists
        if (KeycloakUtil.userExists(usersResource, request.getEmail())) {
            return ResponseEntity.badRequest().body("User already exists!");
        }

        // Create user representation
        UserRepresentation user = new UserRepresentation();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFullName());
        user.setEnabled(true);
        user.setEmailVerified(false);

        // Set password
        CredentialRepresentation password = new CredentialRepresentation();
        password.setType(CredentialRepresentation.PASSWORD);
        password.setValue(request.getPassword());
        password.setTemporary(false); // User won't have to reset password

        user.setCredentials(Collections.singletonList(password));

        // Create the user in Keycloak
        Response response = usersResource.create(user);
        if (response.getStatus() == 201) {
            return ResponseEntity.ok("User registered successfully. Please verify your email.");
        } else {
            return ResponseEntity.status(response.getStatus()).body("Failed to register user.");
        }
    }

    // Implemented login using Direct Grant (Resource Owner Password Credentials) flow
    public ResponseEntity<String> loginUser(LoginRequest request) {
        String tokenUrl = "http://localhost:8080/realms/Lefatshe-Larona/protocol/openid-connect/token";
        RestTemplate restTemplate = new RestTemplate();

        // Prepare the form data for token request
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", "lefatshe-larona-backend");
        formData.add("client_secret", "cOxcIPFhM5SiHZKY4OPw1tAZxpK0bGQp");
        formData.add("grant_type", "password");
        formData.add("username", request.getUsername());

        formData.add("password", request.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);

        try {
            // Call Keycloak token endpoint
            ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                // Return the token response as is, typically a JSON with access_token, refresh_token, etc.
                return ResponseEntity.ok(response.getBody());
            } else {
                return ResponseEntity.status(response.getStatusCode()).body("Authentication failed.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during authentication: " + e.getMessage());
        }
    }
}
