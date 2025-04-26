package lefatshelarona.Database.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // WebSocket endpoint must be public!
                        .requestMatchers("/ws/**").permitAll() // ✅ ADD THIS LINE

                        // auth & public
                        .requestMatchers("/auth/register", "/auth/login", "/auth/refresh").permitAll()
                        .requestMatchers("/auth/check-username/**", "/auth/check-email/**").permitAll()
                        .requestMatchers("/auth/verify-code", "/auth/resend-code").permitAll()
                        .requestMatchers("/users/resendVerification/**").permitAll()
                        .requestMatchers("/oauth2/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/v1/images/upload").permitAll()
                        // secured endpoints...
                        .requestMatchers(HttpMethod.GET, "/communities", "/communities/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/communities").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/communities/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/communities/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/communities/*/join", "/communities/*/leave").authenticated()

                        .requestMatchers(HttpMethod.GET, "/channels", "/channels/{id}").authenticated()
                        .requestMatchers(HttpMethod.POST, "/channels").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/channels/{id}").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/channels/{channelId}/join", "/channels/{channelId}/leave").authenticated()

                        .requestMatchers(HttpMethod.GET, "/queries/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/queries/create").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/queries/update/**").hasAuthority("ROLE_USER")
                        .requestMatchers(HttpMethod.DELETE, "/queries/delete/**").hasAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.GET, "/government-services/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/government-services").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/government-services/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/government-services/**").hasAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.GET, "/notifications/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/notifications/send").hasAuthority("ROLE_ADMIN")

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
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
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess != null && realmAccess.containsKey("roles")) {
                @SuppressWarnings("unchecked")
                List<String> roles = (List<String>) realmAccess.get("roles");
                for (String role : roles) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                }
            }
            return authorities;
        });
        return converter;
    }
}
