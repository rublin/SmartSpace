package org.rublin.util;

import java.util.ResourceBundle;

/**
 * Read properties from file and create constants
 *
 * @author Ruslan Sheremet
 * @see ResourceBundle
 * @since 1.0
 */
public class Resources {
    /**
     * Read the properties file (resources/notification/mail.properties)
     */
    private static final ResourceBundle PROPERTIES = ResourceBundle.getBundle("notification.mail");

    public static final boolean USE_MAIL_NOTIFICATION = Boolean.parseBoolean(PROPERTIES.getString("mail.notification"));
    public static final String SMTP = PROPERTIES.getString("mail.smtp");
    public static final String PORT = PROPERTIES.getString("mail.port");
    public static final String LOGIN = PROPERTIES.getString("mail.login");
    public static final String PASSWORD = PROPERTIES.getString("mail.password");
    public static final String FROM = PROPERTIES.getString("mail.from");
    public static final boolean USE_TELEGRAM_NOTIFICATION = Boolean.parseBoolean(PROPERTIES.getString("telegram.bot"));
    public static final String TELEGRAM_BOT_NAME = PROPERTIES.getString("telegram.bot.username");
    public static final String TELEGRAM_TOKEN = PROPERTIES.getString("telegram.bot.token");
    public static final String SMS_PORT = PROPERTIES.getString("sms.port");
    public static final boolean USE_SMS = Boolean.valueOf(PROPERTIES.getString("sms.send"));
}
