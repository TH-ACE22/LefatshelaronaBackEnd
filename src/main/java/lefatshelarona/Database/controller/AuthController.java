package lefatshelarona.Database.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lefatshelarona.Database.dto.LoginRequest;
import lefatshelarona.Database.dto.RegisterRequest;
import lefatshelarona.Database.dto.ResendCodeRequest;
import lefatshelarona.Database.dto.VerifyCodeRequest;
import lefatshelarona.Database.dto.VerifyCodeSplitInput;
import lefatshelarona.Database.repository.PendingUserRepository;
import lefatshelarona.Database.repository.UserRepository;
import lefatshelarona.Database.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Handles user registration, verification, and login")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final PendingUserRepository pendingUserRepository;

    public AuthController(
            AuthService authService,
            UserRepository userRepository,
            PendingUserRepository pendingUserRepository
    ) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.pendingUserRepository = pendingUserRepository;
    }

    // ✅ Register a new user
    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account in Keycloak and emails a verification code."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input"),
            @ApiResponse(responseCode = "409", description = "Conflict - Email already exists"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return authService.registerUser(request);
    }

    // ✅ Verify with single code
    @Operation(
            summary = "Verify email using full 6-digit code",
            description = "Verifies user account using a full 6-digit code input."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email verified successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired code"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody VerifyCodeRequest request) {
        return authService.verifyCode(request.getEmail(), request.getCode());
    }

    // ✅ Verify with split input
    @Operation(
            summary = "Verify email using 6-digit split code",
            description = "Accepts 6 separate digits and verifies email using the combined code."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email verified successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired code"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping("/verify-code-split")
    public ResponseEntity<?> verifyCodeSplit(@RequestBody VerifyCodeSplitInput input) {
        String code = input.getDigit1() + input.getDigit2() + input.getDigit3()
                + input.getDigit4() + input.getDigit5() + input.getDigit6();
        return authService.verifyCode(input.getEmail(), code);
    }

    // ✅ Resend code
    @Operation(
            summary = "Resend verification code",
            description = "Generates and sends a new 6-digit code if the previous one expired."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Code resent successfully"),
            @ApiResponse(responseCode = "404", description = "User not found or not pending verification"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping("/resend-code")
    public ResponseEntity<?> resendVerificationCode(@RequestBody ResendCodeRequest request) {
        return authService.resendVerificationCode(request.getEmail());
    }

    // ✅ Check username availability
    @Operation(summary = "Check if username is available")
    @GetMapping("/check-username/{username}")
    public ResponseEntity<Boolean> checkUsername(@PathVariable String username) {
        boolean existsInUsers = userRepository.existsByUsernameIgnoreCase(username);
        boolean existsInPending = pendingUserRepository.existsByUsernameIgnoreCase(username);
        return ResponseEntity.ok(!(existsInUsers || existsInPending));
    }

    // ✅ Check email availability
    @Operation(summary = "Check if email is available")
    @GetMapping("/check-email/{email}")
    public ResponseEntity<Boolean> checkEmail(@PathVariable String email) {
        boolean existsInUsers = userRepository.existsByEmailIgnoreCase(email);
        boolean existsInPending = pendingUserRepository.existsByEmailIgnoreCase(email);
        return ResponseEntity.ok(!(existsInUsers || existsInPending));
    }

    // ✅ Login
    @Operation(
            summary = "Login user",
            description = "Authenticates the user using Keycloak Direct Grant flow and returns a JWT token."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return authService.loginUser(request);
    }

    // ✅ Validate token endpoint
    @Operation(
            summary = "Validate JWT token",
            description = "Verifies the authenticity and validity of a JWT token issued by Keycloak."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token is valid"),
            @ApiResponse(responseCode = "401", description = "Token is invalid or expired")
    })
    @GetMapping("/validate-token")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Missing or malformed Authorization header");
        }

        String token = bearerToken.substring(7); // Strip "Bearer "
        boolean valid = authService.validateToken(token);

        if (valid) {
            return ResponseEntity.ok("Token is valid");
        } else {
            return ResponseEntity.status(401).body("Token is invalid or expired");
        }
    }
    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
        return authService.refreshAccessToken(refreshToken);
    }

    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequest request) {
        return authService.registerAdmin(request);
    }


}