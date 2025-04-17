package lefatshelarona.Database.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "email_verification_codes")
public class EmailVerificationCode {

    @Id
    private String email;

    private String code;
    private String keycloakId;
    private LocalDateTime expirationTime;

    private int resendCount;
    private LocalDateTime lastResendTime;

    // ✅ Custom constructor used in registerUser()
    public EmailVerificationCode(String email, String code, String keycloakId, LocalDateTime expirationTime) {
        this.email = email;
        this.code = code;
        this.keycloakId = keycloakId;
        this.expirationTime = expirationTime;
        this.resendCount = 0;
        this.lastResendTime = LocalDateTime.now();
    }
}
