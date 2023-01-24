package pl.kedziorek.mpkoperator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.kedziorek.mpkoperator.config.exception.ResourceNotFoundException;
import pl.kedziorek.mpkoperator.domain.User;
import pl.kedziorek.mpkoperator.domain.dto.request.EmailToUserRequest;
import pl.kedziorek.mpkoperator.repository.UserRepository;
import pl.kedziorek.mpkoperator.service.EmailService;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;

    @Override
    public void sendMail(SimpleMailMessage msg) {
        if (msg != null) {
            javaMailSender.send(msg);
            log.info("Mail has been send");
        } else {
            log.info("Failed to send mail");
        }
    }

    @Override
    public SimpleMailMessage prepareInfoMailAboutCreatedAccount(
            Long id,
            String email,
            String name,
            String password,
            String createdBy) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);

        // TODO mail zmienic ewentualnie na jakis inzynierkowy juz
        msg.setFrom("mpkOperator@email.com");

        msg.setSubject("MPKOperator account creating");

        msg.setText("Hello " + name + "! \n\n" +
                "Your account has been successfully created by " + createdBy + ". \n\n" +
                "Your username: name_surname_id \n" +
                "Your id: " + id + " \n" +
                "Your password: " + password + " \n\n" +
                "Example username to login: \n" +
                "Name: Jan \n" +
                "Surname: Kowalski \n" +
                "Id: 1 \n" +
                "Then your username is like: jan_kowalski_1. \n\n" +
                "You can change password in your account settings (and it is advised to do this as fast as you can!). \n\n" +
                "In case some troubles with logging in, contact admin (for example " + createdBy + ").\n\n" +
                "With regards, \n" +
                "MPKOperator");

        return msg;
    }

    @Override
    public SimpleMailMessage prepareMailToResetPassword(String email, String name, String password) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);

        // TODO mail zmienic ewentualnie na jakis inzynierkowy juz
        msg.setFrom("mpkOperator@email.com");

        msg.setSubject("MPKOperator Password Reset");

        msg.setText("Hello " + name + "! \n\n" +
                "Here is your new password: " + password + "\n\n" +
                "You can change password in your account settings (and it is advised to do this as fast as you can!) \n\n" +
                "With regards, \n" +
                "MPKOperator");

        return msg;
    }

    @Override
    public SimpleMailMessage prepareMailToUserFromUserDetails(EmailToUserRequest emailToUserRequest, UUID uuid) {

        SimpleMailMessage msg = new SimpleMailMessage();

        User userTo = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("User not found in the database"));

        User userFrom = userRepository.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .orElseThrow(() -> new ResourceNotFoundException("User not found in the database"));

        msg.setTo(userTo.getEmail());
        msg.setFrom(userFrom.getEmail());
        msg.setSubject(emailToUserRequest.getSubject());
        msg.setText(emailToUserRequest.getContent());

        return msg;
    }
}
