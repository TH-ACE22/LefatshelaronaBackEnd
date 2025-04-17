package lefatshelarona.Database.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VerifyCodeRequest {

    @Schema(example = "example@gmail.com", description = "Email address of the user")
    private String email;

    @Schema(example = "123456", description = "Full 6-digit verification code")
    private String code;
}
