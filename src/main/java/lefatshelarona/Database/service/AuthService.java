package lefatshelarona.Database.service;

import lefatshelarona.Database.dto.LoginRequest;
import lefatshelarona.Database.dto.RegisterRequest;
import lefatshelarona.Database.model.EmailVerificationCode;
import lefatshelarona.Database.model.PendingUser;
import lefatshelarona.Database.repository.EmailVerificationCodeRepository;
import lefatshelarona.Database.repository.PendingUserRepository;
import lefatshelarona.Database.repository.UserRepository;
import lefatshelarona.Database.util.EmailService;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.net.URLDecoder;
import  java.net.URL;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
@Service
public class AuthService {

    private final Keycloak keycloak;
    private final PendingUserRepository pendingUserRepository;
    private final EmailVerificationCodeRepository codeRepository;
    private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    public AuthService(
            Keycloak keycloak,
            PendingUserRepository pendingUserRepository,
            EmailVerificationCodeRepository codeRepository,
            EmailService emailService,
            UserRepository userRepository // placeholder
    ) {
        this.keycloak = keycloak;
        this.pendingUserRepository = pendingUserRepository;
        this.codeRepository = codeRepository;
        this.emailService = emailService;
    }

    public boolean isEmailAvailable(String email) {
        try {
            String decodedEmail = URLDecoder.decode(email, StandardCharsets.UTF_8);
            UsersResource usersResource = keycloak.realm("Lefatshe-Larona").users();
            return usersResource.search(null, null, null, decodedEmail, 0, 1).isEmpty();
        } catch (Exception e) {
            logger.error("Email availability check failed", e);
            return false;
        }
    }

    public boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWKSet jwkSet = JWKSet.load(new URL("http://localhost:8080/realms/lefatshe-larona/protocol/openid-connect/certs"));
            RSAKey rsaKey = (RSAKey) jwkSet.getKeys().get(0);
            JWSVerifier verifier = new RSASSAVerifier(rsaKey);
            boolean isValid = signedJWT.verify(verifier);
            boolean notExpired = new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime());
            return isValid && notExpired;
        } catch (Exception e) {
            logger.error("Token validation failed", e);
            return false;
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
        keycloakUser.setFirstName(request.getFirstName());
        keycloakUser.setLastName(request.getLastName());
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
                String locationHeader = response.getHeaderString("Location");
                String userId = locationHeader != null
                        ? locationHeader.substring(locationHeader.lastIndexOf("/") + 1)
                        : null;

                if (userId != null) {
                    try {
                        // Assign default role
                        RoleRepresentation userRole = keycloak.realm("Lefatshe-Larona")
                                .roles()
                                .get("ROLE_USER")
                                .toRepresentation();

                        keycloak.realm("Lefatshe-Larona")
                                .users()
                                .get(userId)
                                .roles()
                                .realmLevel()
                                .add(Collections.singletonList(userRole));

                        // Generate verification code
                        String code = String.valueOf((int)(Math.random() * 900000) + 100000);
                        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(10);

                        EmailVerificationCode verification = new EmailVerificationCode(
                                request.getEmail(), code, userId, expiresAt
                        );
                        codeRepository.save(verification);

                        // Send the code to user's email
                        emailService.sendVerificationCode(request.getEmail(), code);

                        // Save pending user

                        PendingUser pendingUser = new PendingUser(
                                userId,
                                request.getUsername(),
                                request.getEmail(),
                                request.getFirstName(),
                                request.getLastName(),
                                request.getPhone(),
                                "ROLE_USER",
                                LocalDateTime.now()
                        );

                        pendingUserRepository.save(pendingUser);

                        return ResponseEntity.ok("Verification code sent to email.");

                    } catch (Exception e) {
                        logger.error("Error during role assignment or code sending", e);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(Map.of("error", "User created, but failed to assign role or send code."));
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Map.of("error", "User created, but user ID is missing."));
                }
            } else {
                logger.error("Keycloak user creation failed: {}", response.getStatus());
                return ResponseEntity.status(response.getStatus())
                        .body(Map.of("error", "Failed to register user in Keycloak."));
            }
        }
    }

    public ResponseEntity<?> verifyCode(String email, String code) {
        EmailVerificationCode record = codeRepository.findById(email).orElse(null);
        if (record == null || !record.getCode().equals(code)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification code.");
        }

        if (record.getExpirationTime().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verification code expired.");
        }

        try {
            UserRepresentation user = keycloak.realm("Lefatshe-Larona")
                    .users()
                    .get(record.getKeycloakId())
                    .toRepresentation();

            user.setEmailVerified(true);

            keycloak.realm("Lefatshe-Larona")
                    .users()
                    .get(record.getKeycloakId())
                    .update(user);

            codeRepository.deleteById(email);
            return ResponseEntity.ok("Email verified successfully.");
        } catch (Exception e) {
            logger.error("Failed to verify email in Keycloak", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to verify email in Keycloak.");
        }
    }

    public ResponseEntity<?> resendVerificationCode(String email) {
        try {
            System.out.println("🔁 Resending verification code for: " + email);

            // Find the pending user by email.
            PendingUser user = pendingUserRepository.findAll()
                    .stream()
                    .filter(p -> p.getEmail().equalsIgnoreCase(email))
                    .findFirst()
                    .orElse(null);

            if (user == null) {
                System.err.println("❌ Pending user not found for email: " + email);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found or not pending verification.");
            }

            // Check Keycloak to see if the user is already verified.
            UserRepresentation kcUser = keycloak.realm("Lefatshe-Larona")
                    .users()
                    .get(user.getKeycloakId())
                    .toRepresentation();
            if (kcUser.isEmailVerified()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("User is already verified.");
            }

            // Retrieve any existing verification record.
            EmailVerificationCode verification = codeRepository.findById(email).orElse(null);
            int maxResendAttempts = 3;
            // Minimum delay between sends (e.g., 2 minutes)
            LocalDateTime now = LocalDateTime.now();
            int delayMinutes = 2;

            if (verification != null) {
                if (verification.getLastResendTime() != null &&
                        verification.getLastResendTime().plusMinutes(delayMinutes).isAfter(now)) {
                    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                            .body("Please wait a while before requesting a new code.");
                }
                if (verification.getResendCount() >= maxResendAttempts) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Maximum resend attempts reached.");
                }
            } else {
                // If no record exists, create a new one with default values.
                verification = new EmailVerificationCode();
                verification.setEmail(email);
                verification.setKeycloakId(user.getKeycloakId());
                verification.setResendCount(0);
            }

            // Generate new code and update the record
            String newCode = String.valueOf((int) (Math.random() * 900000) + 100000);
            LocalDateTime newExpiry = now.plusMinutes(10);
            verification.setCode(newCode);
            verification.setExpirationTime(newExpiry);
            verification.setResendCount(verification.getResendCount() + 1);
            verification.setLastResendTime(now);

            codeRepository.save(verification);

            // Resend the verification code via email
            emailService.sendVerificationCode(email, newCode);
            System.out.println("✅ Verification code resent to: " + email);

            return ResponseEntity.ok("New verification code sent to your email.");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Failed to resend verification code for email: " + email);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to resend verification code.");
        }
    }



    public ResponseEntity<String> loginUser(LoginRequest request) {
        String tokenUrl = "http://localhost:8080/realms/Lefatshe-Larona/protocol/openid-connect/token";

        RestTemplate restTemplate = new RestTemplate();
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
            ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, entity, String.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            logger.error("Login failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Login error: " + e.getMessage());
        }
    }
}
