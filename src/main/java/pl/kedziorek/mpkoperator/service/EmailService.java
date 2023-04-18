package pl.kedziorek.mpkoperator.service;

import org.springframework.mail.SimpleMailMessage;
import pl.kedziorek.mpkoperator.domain.dto.request.EmailToUserRequest;

import java.util.UUID;

public interface EmailService {
    void sendMail(SimpleMailMessage msg);

    SimpleMailMessage prepareInfoMailAboutCreatedAccount(
            Long id,
            String email,
            String name,
            String password,
            String createdBy);

    SimpleMailMessage prepareMailToResetPassword(String email, String name, String password);

    SimpleMailMessage prepareMailToUserFromUserDetails(EmailToUserRequest emailToUserRequest, UUID uuid);
}
