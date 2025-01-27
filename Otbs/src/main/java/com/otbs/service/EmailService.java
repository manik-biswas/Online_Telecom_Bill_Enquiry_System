package com.otbs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    
    public void sendEmail(String toEmail, String subject, String messageText) {
//        SimpleMailMessage message = new SimpleMailMessage(); 
//        message.setTo(toEmail);
//        message.setSubject(subject);
//        message.setText(messageText);
//        mailSender.send(message);
        try {
        
        	MimeMessage message = mailSender.createMimeMessage();
        	MimeMessageHelper helper = new MimeMessageHelper(message, true);

        	helper.setTo(toEmail);
        	helper.setSubject(subject);
        	helper.setText(messageText);
        	mailSender.send(message);
        	}
        	catch (MessagingException e) {
        		e.printStackTrace();
        	}
    	}

    //Registration Email sending by Team 1
    public void sendThankYouEmail(String toEmail, String username) {
        try {
            String subject = "Welcome to Bharat Teleservices!";
            String body = String.format(
                "Dear %s,\n\nThank you for registering with Bharat Teleservices!\n\nBest Regards,\nTeam Bharat Teleservices",
                username);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
