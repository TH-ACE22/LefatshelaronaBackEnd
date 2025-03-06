package lefatshelarona.Database.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public Endpoints
                        .requestMatchers("/auth/register", "/auth/login").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // Query Endpoints
                        .requestMatchers(HttpMethod.GET, "/queries/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/queries/create").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/queries/update/**").hasAuthority("USER")
                        .requestMatchers(HttpMethod.DELETE, "/queries/delete/**").hasAuthority("ADMIN")

                        // Channel Endpoints
                        .requestMatchers(HttpMethod.GET, "/channels/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/channels/create").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/channels/delete/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/channels/join").authenticated()
                        .requestMatchers(HttpMethod.POST, "/channels/leave").authenticated()

                        // Government Service Endpoints
                        .requestMatchers(HttpMethod.GET, "/government-services/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/government-services").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/government-services/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/government-services/**").hasAuthority("ADMIN")
                        //Government Servant Endpoints

                        .requestMatchers(HttpMethod.GET, "/government-servants/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/government-servants").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/government-servants/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/government-servants/**").hasRole("ADMIN")

                       //communities
                        .requestMatchers(HttpMethod.POST, "/communities/{communityId}/join/{userId}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/communities/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/communities").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/communities/**").hasAuthority("ADMIN")
                          //notification
                        .requestMatchers(HttpMethod.GET, "/notifications/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/notifications/send").hasAuthority("ADMIN")


                        // Any Other Endpoints Require Authentication
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    // CORS Configuration
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("")); // Adjust this based on your frontend URL
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
