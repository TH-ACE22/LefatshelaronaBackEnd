package lefatshelarona.Database.repository;

import lefatshelarona.Database.model.EmailVerificationCode;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailVerificationCodeRepository extends MongoRepository<EmailVerificationCode, String> {
}
