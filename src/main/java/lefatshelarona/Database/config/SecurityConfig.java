package lefatshelarona.Database.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/register", "/auth/login").permitAll()
                        .requestMatchers("/auth/check-username/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/users/resendVerification/**").permitAll()
                        .requestMatchers("/auth/check-email/**").permitAll()
                        .requestMatchers("/auth/verify-code").permitAll()
                        .requestMatchers("/auth/resend-code").permitAll()

                        .requestMatchers("/oauth2/**").permitAll()
                        // Queries
                        .requestMatchers(HttpMethod.GET, "/queries/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/queries/create").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/queries/update/**").hasAuthority("ROLE_USER")
                        .requestMatchers(HttpMethod.DELETE, "/queries/delete/**").hasAuthority("ROLE_ADMIN")

                        // Channels
                        .requestMatchers(HttpMethod.GET, "/channels/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/channels/create").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/channels/delete/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/channels/join").authenticated()
                        .requestMatchers(HttpMethod.POST, "/channels/leave").authenticated()

                        // Government Services
                        .requestMatchers(HttpMethod.GET, "/government-services/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/government-services").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/government-services/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/government-services/**").hasAuthority("ROLE_ADMIN")

                        // Notifications
                        .requestMatchers(HttpMethod.GET, "/notifications/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/notifications/send").hasAuthority("ROLE_ADMIN")

                        // Other endpoints require authentication
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> authorities = new ArrayList<>();

            // Extract roles from the nested "realm_access" claim
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess != null && realmAccess.containsKey("roles")) {
                @SuppressWarnings("unchecked")
                List<String> roles = (List<String>) realmAccess.get("roles");
                authorities.addAll(roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .toList());
            }
            return authorities;
        });
        return converter;
    }
}
