package lefatshelarona.Database.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationCode(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Lefatshe-Larona Email Verification Code");
        message.setText("Hello,\n\nYour verification code is: " + code +
                "\n\nThis code is valid for 10 minutes." +
                "\n\nThank you for registering with Lefatshe-Larona.");
        message.setFrom("letshwititshenolo21@gmail.com"); // optional but good practice
        mailSender.send(message);
    }
}
