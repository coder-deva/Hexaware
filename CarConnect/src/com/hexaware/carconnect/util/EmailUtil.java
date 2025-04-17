package com.hexaware.carconnect.util;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailUtil {

    public static void sendEmail(String toEmail, String subject, String body) throws MessagingException {
       
        final String fromEmail = "2116012@saec.ac.in";
        final String password = "pmqa yyrn gfzo jnpa"; 

        
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); 
        props.put("mail.smtp.port", "587"); 
        props.put("mail.smtp.auth", "true"); 
        props.put("mail.smtp.starttls.enable", "true");

        
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(subject);
        message.setText(body);

        
        Transport.send(message);

        System.out.println("Confirmation email sent to " + toEmail);
    }
}
