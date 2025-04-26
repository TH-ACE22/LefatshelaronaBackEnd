// src/main/java/lefatshelarona/database/dto/ReportRequest.java
package  lefatshelarona.Database.dto;

import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Payload for submitting a new report.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportRequest {

    /** The textual content of the report. */
    @NotBlank(message = "Message must not be blank")
    private String message;

    /** List of Cloudinary URLs for attachments; can be empty. */
    @NotEmpty(message = "At least one attachment URL is required")
    private List<@NotBlank(message = "Attachment URL must not be blank") String> attachments;

    /** ID of the channel (e.g., “police”, “water-utilities”) this report belongs to. */
    @NotBlank(message = "Channel ID must not be blank")
    private String channelId;
}
