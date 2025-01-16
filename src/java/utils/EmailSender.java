/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Omar
 */
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {

    public static void sendEmail(final String from, final String password, final String to, final String restCode, String operation) throws Exception {

//        final String from = "fleetiqg5@gmail.com";
//        final String password = "whcn jgsp rcmw jdms";
        String subject = "";
        String body = "";

        if (operation.equalsIgnoreCase("rest")) {
            subject = "Password Reset Request";
            body = "Hello,\n\n"
                            + "Use the following code to reset your password: \n\n"
                            + "Reset Code:" + restCode + "\n\n"
                            + "If you did not request a password reset, please ignore this email.\n\n"
                            + "Best regards,\n\n"
                            + "FleetIQ";
        } else if (operation.equalsIgnoreCase("changed")) {
            subject = "Password has been changed successfully";
            body = "Hello,\n\n"
                            + "Your password has been successfully changed. If you did not make this change, please contact us immediately.\n\n"
                            + "If you have any questions or need further assistance, feel free to reach out.\n\n"
                            + "Best regards,\n\n"
                            + "FleetIQ";
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email sent successfully to " + to);
        } catch (MessagingException e) {
            System.err.println("Error sending email: " + e.getMessage());
            throw e;
        }
    }
}
