package lefatshelarona.Database.controller;

import lefatshelarona.Database.model.Report;
import lefatshelarona.Database.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@Tag(name = "Report Management", description = "Operations for submitting community reports")
public class ReportController {

    private final ReportService reportService;

    @Operation(
            summary = "Submit a report",
            description = "Allows users to submit a report with a message and Cloudinary file URLs",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Report successfully submitted",
                            content = @Content(schema = @Schema(implementation = Report.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Missing or invalid request parameters"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<Report> submitReport(
            @Parameter(description = "Text message describing the report", example = "Broken water pipe near Block C")
            @RequestParam("message") String message,

            @Parameter(
                    description = "List of attachment URLs (uploaded via Cloudinary)",
                    example = "[\"https://res.cloudinary.com/demo/image/upload/sample1.jpg\"]"
            )
            @RequestParam("urls") List<String> urls,

            @Parameter(description = "Channel ID this report belongs to", example = "water-utilities")
            @RequestParam("channelId") String channelId,

            JwtAuthenticationToken auth
    ) {
        String userId = auth.getToken().getSubject();
        Report saved = reportService.saveReport(message, urls, userId, channelId);
        return ResponseEntity.ok(saved);
    }
}
