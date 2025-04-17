package lefatshelarona.Database.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VerifyCodeSplitInput {

    @Schema(example = "example@gmail.com", description = "Email address of the user")
    private String email;

    @Schema(example = "1", description = "First digit of the code")
    private String digit1;

    @Schema(example = "2", description = "Second digit of the code")
    private String digit2;

    @Schema(example = "3", description = "Third digit of the code")
    private String digit3;

    @Schema(example = "4", description = "Fourth digit of the code")
    private String digit4;

    @Schema(example = "5", description = "Fifth digit of the code")
    private String digit5;

    @Schema(example = "6", description = "Sixth digit of the code")
    private String digit6;
}
