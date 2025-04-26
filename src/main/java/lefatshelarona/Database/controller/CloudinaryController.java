package  lefatshelarona.Database.controller;

import lefatshelarona.Database.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
@CrossOrigin(origins = "*")
public class CloudinaryController {

    private final CloudinaryService cloudinaryService;

    @PostMapping(
            value    = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, Object>> uploadImage(
            @RequestPart("file") MultipartFile file
    ) {
        if (file.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No file provided"
            );
        }

        try {
            Map<String, Object> result = cloudinaryService.uploadFile(file);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(result);
        } catch (IOException ex) {
            log.error("Failed to upload to Cloudinary", ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Image upload failed"
            );
        }
    }
}
