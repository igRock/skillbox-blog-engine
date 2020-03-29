package ru.skillbox.blog_engine.services;

import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService {

    @Value("${spring.mail.username}")
    private String emailFrom;
    @Value("${spring.mail.password}")
    private String emailFromPassword;

    public void send(String recipientEmail, String subject, String msg) throws Exception {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailFrom, emailFromPassword);
            }
        };
        Session session = Session.getDefaultInstance(properties, auth);

        MimeMessage message = new MimeMessage(session); // email message
        message.setFrom(new InternetAddress(emailFrom)); // setting header fields
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        message.setSubject(subject); // subject line
        message.setText(msg);
        Transport.send(message);
    }
}
