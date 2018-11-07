package org.rublin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.List;
import java.util.Properties;

/**
 * Created by Sheremet on 21.08.2016.
 */
@Slf4j
@Controller
public class EmailController {
    
    @Value("${mail.smtp}")
    private String smtp;

    @Value("${mail.port}")
    private String port;

    @Value("${mail.login}")
    private String login;

    @Value("${mail.password}")
    private String password;

    @Value("${mail.from}")
    private String from;

    private  Properties getMailProperties() {
        Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", "true");
        mailProperties.put("mail.smtp.socketFactory.port", port);
        mailProperties.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        mailProperties.put("mail.smtp.host", smtp);
        mailProperties.put("mail.smtp.port", port);
        mailProperties.put("mail.smtp.login", login);
        mailProperties.put("mail.smtp.password", password);
        mailProperties.put("mail.smtp.from", from);
        return mailProperties;
    }

    public void sendMailWithAttach(String subject, String text, List<File> photos, List<String> emails) {
            Session session = Session.getInstance(getMailProperties(),
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(login, password);
                        }
                    });
            try{
                // Create a default MimeMessage object.
                MimeMessage message = new MimeMessage(session);

                // Set From: header field of the header.
                message.setFrom(new InternetAddress(from));

                // Set To: header field of the header.
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(String.join(", ", emails)));

                // Set Subject: header field
                message.setSubject(subject);

                // This mail has 2 part, the BODY and the embedded image
                MimeMultipart multipart = new MimeMultipart("related");

                // first part (the html)
                BodyPart messageBodyPart = new MimeBodyPart();
                String htmlText = text + "<img src=\"cid:image\">";
                messageBodyPart.setContent(htmlText, "text/html");
                // add it
                multipart.addBodyPart(messageBodyPart);

                // second part (the image)
                messageBodyPart = new MimeBodyPart();
                for (File  photo : photos) {
                    String filename = photo.getPath();

                    DataSource fds = new FileDataSource(filename);

                    messageBodyPart.setDataHandler(new DataHandler(fds));
                    messageBodyPart.setHeader("Content-ID", "<image>");

                    // add image to the multipart
                    multipart.addBodyPart(messageBodyPart);
                }
                // Send the complete message parts
                message.setContent(multipart );

                // Send message
                Transport.send(message);
                log.info("Mail {} send success", subject);
            }catch (MessagingException e) {
                log.error("Mail {} send error. Exception is: {}", subject, e.getMessage());
            }
    }

    public void sendMail(String subject, String text, List<String> emails) {
        Session session = Session.getInstance(getMailProperties(),
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(login, password);
                        }
                    });

        try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(String.join(", ", emails)));
                message.setSubject(subject);
                message.setContent(text, "text/html; charset=UTF-8");

                Transport.send(message);
                log.info("Mail {} to {} send success", subject, message.getAllRecipients());
            } catch (MessagingException e) {
                log.error("Mail {} send error. Exception is: {}", subject, e.getMessage());
                throw new RuntimeException(e);
            }
        }


}
