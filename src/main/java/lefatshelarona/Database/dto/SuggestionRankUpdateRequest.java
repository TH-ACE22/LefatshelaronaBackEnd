package lefatshelarona.Database.dto;

import lombok.Data;

@Data
public class SuggestionRankUpdateRequest {
    private String suggestionId;
    private int newRank;

}
