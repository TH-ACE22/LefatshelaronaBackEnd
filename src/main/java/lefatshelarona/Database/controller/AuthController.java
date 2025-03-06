package lefatshelarona.Database.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lefatshelarona.Database.dto.RegisterRequest;
import lefatshelarona.Database.dto.LoginRequest;
import lefatshelarona.Database.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        String result = authService.registerUser(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String token = authService.loginUser(request);
        if (token != null) {
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}
