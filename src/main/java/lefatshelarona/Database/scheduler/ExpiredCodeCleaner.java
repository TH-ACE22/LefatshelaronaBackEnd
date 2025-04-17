package lefatshelarona.Database.scheduler;

import lefatshelarona.Database.repository.EmailVerificationCodeRepository;
import lefatshelarona.Database.model.EmailVerificationCode;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExpiredCodeCleaner {

    private final EmailVerificationCodeRepository codeRepository;

    // Every hour: delete expired codes
    @Scheduled(fixedRate = 3600000) // 1 hour = 3600000 ms
    public void cleanExpiredCodes() {
        List<EmailVerificationCode> allCodes = codeRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        allCodes.stream()
                .filter(code -> code.getExpirationTime().isBefore(now))
                .forEach(code -> codeRepository.deleteById(code.getEmail()));

        System.out.println("🧹 Cleaned expired verification codes");
    }
}
