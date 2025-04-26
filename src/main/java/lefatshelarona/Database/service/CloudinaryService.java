package lefatshelarona.Database.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @SuppressWarnings("unchecked")
    public Map<String, Object> uploadFile(MultipartFile file) throws IOException {
        return (Map<String, Object>) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
    }
}
