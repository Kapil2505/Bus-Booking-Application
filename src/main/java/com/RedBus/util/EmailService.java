package com.RedBus.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMailWithAttachment(String to, String subject, String text, String attachmentFileName, byte[] attachmentData) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;

        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(senderEmail, "Your Sender Name");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            // Create a DataSource from the byte array
            DataSource attachment = new ByteArrayDataSource(attachmentData, "application/pdf");

            // Attach the PDF to the email
            helper.addAttachment(attachmentFileName, attachment);

            // Send the email
            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }
}
