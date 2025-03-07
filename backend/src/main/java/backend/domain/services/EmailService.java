package backend.domain.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendConfirmationEmail(String toEmail, String body) {
        Mono.create((sink) -> {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
                mimeMessageHelper.setTo(toEmail);
                mimeMessageHelper.setSubject("Confirm email");
                mimeMessageHelper.setText("<html>" +
                                "<body>" +
                                "<p>Click the button below to complete registration:</p>" +
                                "<a href=\"" + body + "\" style=" +
                                "\"display: inline-block; background-color: #004CBB; color: white; padding: 5px 10px; text-align: center; text-decoration: none; font-size: 12px; border-radius: 4px;\">" +
                                "Confirm Registration" +
                                "</a>" +
                                "</body>" +
                                "</html>",
                        true);
                mailSender.send(message);
                sink.success();

            } catch (MessagingException e) {
                sink.error(e);
                System.out.println("Error sending confirmation email: " + e.getMessage());
            }
        }).subscribeOn(Schedulers.boundedElastic()).then();

    }
}
