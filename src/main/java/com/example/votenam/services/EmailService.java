package com.example.votenam.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    public void sendVoteConfirmationEmail(String voterName, String voterEmail, String candidateName, String voteCategory) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            
            helper.setFrom(fromEmail);
            helper.setTo(voterEmail);
            helper.setSubject("Thank You for Voting - Namibia Voting System");
            
            String htmlContent = buildEmailContent(voterName, candidateName, voteCategory);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }
    
    private String buildEmailContent(String voterName, String candidateName, String voteCategory) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 0; background-color: #f4f4f4; }" +
                ".container { max-width: 600px; margin: 0 auto; background-color: #ffffff; }" +
                ".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 30px; text-align: center; }" +
                ".header h1 { color: #ffffff; margin: 0; font-size: 28px; }" +
                ".content { padding: 40px 30px; }" +
                ".greeting { font-size: 18px; color: #333; margin-bottom: 20px; }" +
                ".message { font-size: 16px; color: #555; margin-bottom: 20px; }" +
                ".vote-details { background-color: #f8f9fa; padding: 20px; border-radius: 8px; margin: 20px 0; }" +
                ".vote-details h3 { color: #667eea; margin-top: 0; }" +
                ".vote-details p { margin: 10px 0; }" +
                ".footer { background-color: #2c3e50; color: #ffffff; padding: 30px; text-align: center; }" +
                ".footer p { margin: 5px 0; font-size: 14px; }" +
                ".footer a { color: #3498db; text-decoration: none; }" +
                ".logo-placeholder { background-color: rgba(255,255,255,0.2); padding: 20px; border-radius: 8px; display: inline-block; margin-bottom: 10px; }" +
                ".logo-text { color: #ffffff; font-size: 24px; font-weight: bold; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<div class='logo-placeholder'>" +
                "<div class='logo-text'>🇳🇦 NAMIBIA VOTING SYSTEM</div>" +
                "</div>" +
                "<h1>Vote Confirmation</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<div class='greeting'>Dear " + voterName + ",</div>" +
                "<div class='message'>" +
                "<p>Thank you for participating in the democratic process of Namibia. Your vote has been successfully recorded and is important to our nation's future.</p>" +
                "</div>" +
                "<div class='vote-details'>" +
                "<h3>Your Vote Details:</h3>" +
                "<p><strong>Vote Category:</strong> " + voteCategory + "</p>" +
                "<p><strong>Selected Candidate:</strong> " + candidateName + "</p>" +
                "<p><strong>Status:</strong> <span style='color: #27ae60; font-weight: bold;'>✓ Confirmed</span></p>" +
                "</div>" +
                "<div class='message'>" +
                "<p>Your participation strengthens our democracy. Every vote counts, and your voice matters.</p>" +
                "<p>If you have any questions or concerns about your vote, please contact our support team.</p>" +
                "</div>" +
                "</div>" +
                "<div class='footer'>" +
                "<p><strong>Namibia Voting System</strong></p>" +
                "<p>Official Government Voting Platform</p>" +
                "<p>Email: support@votenam.gov.na | Phone: +264 61 XXX XXXX</p>" +
                "<p style='font-size: 12px; margin-top: 20px;'>This is an automated confirmation email. Please do not reply to this message.</p>" +
                "<p style='font-size: 12px;'>© " + java.time.Year.now() + " Namibia Voting System. All rights reserved.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}

