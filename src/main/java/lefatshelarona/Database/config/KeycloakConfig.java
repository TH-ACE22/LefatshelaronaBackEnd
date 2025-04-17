package lefatshelarona.Database.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl("http://localhost:8080")
                .realm("master") // Use the "master" realm or your dedicated admin realm
                .clientId("admin-cli")
                .username("admin")  // Replace with your actual admin username
                .password("TH01")  // Replace with your actual admin password
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }

}
