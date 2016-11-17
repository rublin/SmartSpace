package org.rublin.controller;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.util.List;
import java.util.Properties;

import static org.slf4j.LoggerFactory.getLogger;
import static org.rublin.util.Resources.*;

/**
 * Created by Sheremet on 21.08.2016.
 */
@Controller
public class EmailController {
    private static final Logger LOG = getLogger(EmailController.class);


    private  Properties getMailProperties() {
        Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", "true");
        mailProperties.put("mail.smtp.socketFactory.port", PORT);
        mailProperties.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        mailProperties.put("mail.smtp.host", SMTP);
        mailProperties.put("mail.smtp.port", PORT);
        mailProperties.put("mail.smtp.login", LOGIN);
        mailProperties.put("mail.smtp.password", PASSWORD);
        mailProperties.put("mail.smtp.from", FROM);
        return mailProperties;
    }

    private String getUserEmails(List<String> emails) {
        return String.join(", ", emails);
    }
    public void sendMailWithAttach(String subject, String text, List<File> photos, List<String> emails) {
            Session session = Session.getInstance(getMailProperties(),
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(LOGIN, PASSWORD);
                        }
                    });
            try{
                // Create a default MimeMessage object.
                MimeMessage message = new MimeMessage(session);

                // Set From: header field of the header.
                message.setFrom(new InternetAddress(FROM));

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
                LOG.info("Mail {} send success", subject);
            }catch (MessagingException e) {
                LOG.error("Mail {} send error. Exception is: {}", subject, e.getMessage());
            }
    }

    public void sendMail(String subject, String text, List<String> emails) {
        Session session = Session.getInstance(getMailProperties(),
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(LOGIN, PASSWORD);
                        }
                    });

        try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(FROM));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(String.join(", ", emails)));
                message.setSubject(subject);
                message.setContent(text, "text/html");

                Transport.send(message);
                LOG.info("Mail {} send success", subject);
            } catch (MessagingException e) {
                LOG.error("Mail {} send error. Exception is: {}", subject, e.getMessage());
                throw new RuntimeException(e);
            }
        }
}
