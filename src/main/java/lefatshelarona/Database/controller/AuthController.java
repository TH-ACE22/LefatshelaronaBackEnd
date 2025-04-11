package lefatshelarona.Database.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lefatshelarona.Database.dto.RegisterRequest;
import lefatshelarona.Database.dto.LoginRequest;
import lefatshelarona.Database.service.AuthService;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Handles user registration and login instructions")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account in Keycloak. Users must verify their email before accessing the app."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input"),
            @ApiResponse(responseCode = "409", description = "Conflict - Username or email already exists",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        // Now returns ResponseEntity<?> so that the error response can include a JSON object
        return authService.registerUser(request);
    }

    @Operation(
            summary = "Check if username is available",
            description = "Returns true if the given username is available (i.e., not used by any existing user)."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Availability status returned",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))
    )
    @GetMapping("/check-username/{username}")
    public ResponseEntity<Boolean> checkUsername(@PathVariable String username) {
        boolean isAvailable = authService.isUsernameAvailable(username);
        return ResponseEntity.ok(isAvailable);
    }

    @Operation(
            summary = "Login user",
            description = "Authenticates the user using Keycloak Direct Grant flow and returns a JWT token."
    )
    @ApiResponse(
            responseCode = "200",
            description = "User authenticated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
    )
    @ApiResponse(responseCode = "400", description = "Invalid credentials")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        return authService.loginUser(request);
    }
}
