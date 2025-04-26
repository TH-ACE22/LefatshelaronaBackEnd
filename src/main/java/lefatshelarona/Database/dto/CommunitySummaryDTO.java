package lefatshelarona.Database.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommunitySummaryDTO {
    private String id;
    private String name;
    private String location;
    private String description;
    private int memberCount;
}