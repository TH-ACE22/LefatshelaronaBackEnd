package lefatshelarona.Database.service;

import lefatshelarona.Database.model.Report;
import lefatshelarona.Database.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;

    public Report saveReport(String message, List<String> fileUrls, String userId, String channelId) {
        Report report = new Report();
        report.setMessage(message);
        report.setAttachments(fileUrls);
        report.setSentBy(userId);
        report.setChannelId(channelId);
        report.setSubmittedAt(new Date());

        return reportRepository.save(report);
    }
}
