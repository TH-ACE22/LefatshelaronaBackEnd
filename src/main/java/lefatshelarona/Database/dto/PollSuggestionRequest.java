package lefatshelarona.Database.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PollSuggestionRequest {

    @NotBlank
    private String suggestionText;

    @NotBlank
    private String channelId;

    @NotBlank
    private String communityId;
}
