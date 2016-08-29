package org.rublin.util;

import org.slf4j.Logger;
import sun.misc.BASE64Encoder;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Sheremet on 21.08.2016.
 */
public class Notification {
    private static final String PROPERTIES = "notification/mail.properties";
    private static final Logger LOG = getLogger(Notification.class);
    public static void sendMailWithAttach(String subject, String text, String url) {
        Properties properties = readProperties();
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.host", properties.getProperty("mail.smtp"));
        props.put("mail.smtp.port", "465");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(properties.getProperty("mail.login"), properties.getProperty("mail.password"));
                    }
                });

        String filename = getImageFromCamera(url);

        try{
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(properties.getProperty("mail.from")));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(properties.getProperty("mail.to")));

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
            DataSource fds = new FileDataSource(filename);

            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setHeader("Content-ID", "<image>");

            // add image to the multipart
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart );

            // Send message
            Transport.send(message);
            LOG.info("Mail {} send success", subject);
        }catch (MessagingException e) {
            LOG.error("Mail {} send error. Exception is: {}", subject, e.getMessage());
        }
    }

    public static void sendMail(String subject, String text) {
        Properties properties = readProperties();
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.host", properties.getProperty("mail.smtp"));
        props.put("mail.smtp.port", "465");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(properties.getProperty("mail.login"), properties.getProperty("mail.password"));
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(properties.getProperty("mail.from")));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(properties.getProperty("mail.to")));
            message.setSubject(subject);
            message.setContent(text, "text/html");

            Transport.send(message);
            LOG.info("Mail {} send success", subject);

        } catch (MessagingException e) {
            LOG.error("Mail {} send error. Exception is: {}", subject, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static String  getImageFromCamera(String link) {
        Properties properties = readProperties();
        URL url = null;
        URLConnection connection = null;
        String passStr = properties.getProperty("cam.login") + ":" + properties.getProperty("cam.password");
        String encoding = new BASE64Encoder().encode(passStr.getBytes());
        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-mm")) + ".jpg";
        try {
            url = new URL(link);
            connection = url.openConnection();
            connection.setRequestProperty("Authorization", "Basic "+encoding);
        } catch (Exception e) {
            LOG.error("Error to download image from camera " + link + e.getMessage());
        }
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(filename));
             InputStream in = new BufferedInputStream(connection.getInputStream())
        ){
            byte[] b = new byte[2048];
            int length;
            while ((length = in.read(b)) != -1) {
                out.write(b, 0, length);
            }
        } catch (Exception e) {
            LOG.error("Error to download image from camera " + link + e.getMessage());
        }
        return filename;
    }
    public static Properties readProperties() {
        Properties properties = new Properties();
        try (InputStream input = Notification.class.getClassLoader().getResourceAsStream(PROPERTIES)) {
            properties.load(input);
        } catch (IOException e) {
            LOG.error("Error to read mail.property file. Exception is: " + e.getMessage());
        }
        return properties;
    }

    public static void main(String[] args) {
//        sendMail("Test mail", "Some text here...");
        sendMailWithAttach("test", "Some text and image", "http://192.168.0.31/Streaming/channels/1/picture");
//        System.out.println(getImageFromCamera("http://192.168.0.31/Streaming/channels/1/picture"));
    }
}
