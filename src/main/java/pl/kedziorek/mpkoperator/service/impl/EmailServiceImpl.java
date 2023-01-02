package pl.kedziorek.mpkoperator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.kedziorek.mpkoperator.service.EmailService;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;

    public void sendMail(SimpleMailMessage msg) {
        if (msg != null) {
            javaMailSender.send(msg);
            log.info("Mail has been send");
        } else {
            log.info("Failed to send mail");
        }
    }

    public SimpleMailMessage prepareInfoMailAboutCreatedAccount (
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
                "Then your username is like: jan_kowalski_1). \n\n" +
                "You can change password in your account settings (and it is advised to do this as fast as you can!) \n\n" +
                "In case some troubles with logging in, contact admin (for example " + createdBy + ")\n\n" +
                "With regards, \n" +
                "MPKOperator");

        return msg;
    }
}
