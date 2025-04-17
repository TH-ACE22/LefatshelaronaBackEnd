package lefatshelarona.Database.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;




@Data
public class ResendCodeRequest {
    @Schema(example = "user@example.com", description = "The email address to resend verification code to.")
    private String email;
}
