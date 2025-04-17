package lefatshelarona.Database.scheduler;

import jakarta.ws.rs.NotFoundException;
import lefatshelarona.Database.model.PendingUser;
import lefatshelarona.Database.model.User;
import lefatshelarona.Database.repository.PendingUserRepository;
import lefatshelarona.Database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EmailVerificationChecker {

    private final Keycloak keycloak;
    private final PendingUserRepository pendingUserRepository;
    private final UserRepository userRepository;

    @Scheduled(fixedDelay = 60000) // Runs every 60 seconds
    public void checkVerifiedUsers() {
        List<PendingUser> pendingUsers = pendingUserRepository.findAll();

        for (PendingUser pending : pendingUsers) {
            try {
                String keycloakId = pending.getKeycloakId();
                System.out.println("🔍 Checking user ID: " + keycloakId);

                UserRepresentation kcUser = keycloak.realm("Lefatshe-Larona")
                        .users()
                        .get(keycloakId)
                        .toRepresentation();

                if (kcUser.isEmailVerified()) {
                    // ✅ Build verified user from pending
                    User verifiedUser = new User(
                            pending.getEmail(),
                            pending.getFirstName(),
                            pending.getLastName(),
                            null,                    // community (to be filled later)
                            pending.getPhone(),
                            null,                    // profilePicture (to be filled later)
                            pending.getRole(),
                            pending.getUsername(),
                            pending.getKeycloakId(),
                            true,
                            new ArrayList<>()        // joinedChannels
                    );


                    userRepository.save(verifiedUser);
                    pendingUserRepository.deleteById(keycloakId);

                    System.out.println("✅ Email verified: user " + pending.getUsername() + " moved to users collection.");
                }

            } catch (NotFoundException nf) {
                // User not found in Keycloak
                System.err.println("⚠️ Keycloak user not found: " + pending.getKeycloakId() +
                        " (probably deleted) — removing from pending.");
                pendingUserRepository.deleteById(pending.getKeycloakId());

            } catch (Exception e) {
                System.err.println("❌ Error checking user " + pending.getUsername() + ": " + e.getMessage());
            }
        }
    }
}
