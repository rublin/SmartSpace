package org.rublin.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Sheremet on 21.08.2016.
 */
public class Notification {
    public static void sendMail(String text) {
        Properties properties = new Properties();
        try (InputStream input = Notification.class.getClassLoader().getResourceAsStream("notification/mail.properties")) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.host", properties.getProperty("mail.smtp"));
        props.put("mail.smtp.port", "465");

//        System.out.println(properties.getProperty("mail.login") + " " + properties.getProperty("mail.password"));
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(properties.getProperty("mail.login"), properties.getProperty("mail.password"));
//                        return new PasswordAuthentication("selyschanska19@ukr.net", "Zdct,fxe");
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(properties.getProperty("mail.from")));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(properties.getProperty("mail.to")));
            message.setSubject("SmartSpace notification");
            message.setText(text);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        sendMail("Some text here...");
    }
}
