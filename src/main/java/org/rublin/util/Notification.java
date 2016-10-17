package org.rublin.util;

import org.rublin.model.Camera;
import org.rublin.model.user.User;
import org.slf4j.Logger;
import sun.misc.BASE64Encoder;

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
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Sheremet on 21.08.2016.
 */
public class Notification {
    private static final String PROPERTIES = "notification/mail.properties";
    private static final Logger LOG = getLogger(Notification.class);
    static {
        Properties properties = new Properties();
        try (InputStream input = Notification.class.getClassLoader().getResourceAsStream(PROPERTIES)) {
            properties.load(input);
        } catch (IOException e) {
            LOG.error("Error to read mail.property file. Exception is: " + e.getMessage());
        }
        USE_MAIL_NOTIFICATION = Boolean.parseBoolean(properties.getProperty("mail.notification"));
        SMTP = properties.getProperty("mail.smtp");
        PORT = properties.getProperty("mail.port");
        LOGIN = properties.getProperty("mail.login");
        PASSWORD = properties.getProperty("mail.password");
        FROM = properties.getProperty("mail.from");
        USE_TELEGRAM_NOTIFICATION = Boolean.parseBoolean(properties.getProperty("telegram.bot"));
        TELEGRAM_BOT_NAME = properties.getProperty("telegram.bot.username");
        TELEGRAM_TOKEN = properties.getProperty("telegram.bot.token");


    }
    private static final boolean USE_MAIL_NOTIFICATION;
    private static final String SMTP;
    private static final String PORT;
    private static final String LOGIN;
    private static final String PASSWORD;
    private static final String FROM;
    public static final String TELEGRAM_BOT_NAME;
    public static final String TELEGRAM_TOKEN;
    public static final boolean USE_TELEGRAM_NOTIFICATION;

    private static Properties getMailProperties() {
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

    private static String getUserEmails(List<User> users) {
        StringBuffer emails = new StringBuffer();
        users.forEach(user -> emails.append(user.getEmail() + ", "));
        return emails.substring(0, emails.lastIndexOf(","));
    }
    public static void sendMailWithAttach(String subject, String text, List<File> photos, List<User> users) {
        if (USE_MAIL_NOTIFICATION) {
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
                        InternetAddress.parse(getUserEmails(users)));

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
        } else {
            LOG.info("Mail notification is disable");
        }
    }

    public static void sendMail(String subject, String text, List<User> users) {
        if (USE_MAIL_NOTIFICATION) {
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
                        InternetAddress.parse(getUserEmails(users)));
                message.setSubject(subject);
                message.setContent(text, "text/html");

                Transport.send(message);
                LOG.info("Mail {} send success", subject);
            } catch (MessagingException e) {
                LOG.error("Mail {} send error. Exception is: {}", subject, e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            LOG.info("Mail notification is disable");
        }
    }

    public static String  getImageFromCamera(Camera camera) {
        URL url = null;
        URLConnection connection = null;
        String passStr = camera.getLogin() + ":" + camera.getPassword();
        String encoding = new BASE64Encoder().encode(passStr.getBytes());
        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-mm")) + ".jpg";
        try {
            url = new URL(camera.getURL());
            connection = url.openConnection();
            connection.setRequestProperty("Authorization", "Basic "+encoding);
        } catch (Exception e) {
            LOG.error("Error to download image from camera " + camera.getName() + e.getMessage());
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
            LOG.error("Error to download image from camera " + camera.getName() + e.getMessage());
        }
        return filename;
    }

    public static void main(String[] args) {
//        sendMail("Test mail", "Some text here...");
//        sendMailWithAttach("test", "Some text and image", "http://sd.keepcalm-o-matic.co.uk/i/glory-to-ukraine-and-fuck-putin.png");
//        System.out.println(getImageFromCamera("http://192.168.0.31/Streaming/channels/1/picture"));
    }
}
