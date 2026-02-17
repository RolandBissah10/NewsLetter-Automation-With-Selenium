package org.example.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationManager {

    private static final Logger logger = LoggerFactory.getLogger(NotificationManager.class);

    private static final Properties props = new Properties();

    static {
        try (java.io.InputStream input = NotificationManager.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input != null) {
                props.load(input);
            } else {
                logger.warn("config.properties not found in classpath. Using defaults/environment variables.");
            }
        } catch (Exception e) {
            logger.error("Error loading config.properties", e);
        }
    }

    // Environment variables override properties file
    private static final String SMTP_HOST = System.getenv().getOrDefault("SMTP_HOST",
            props.getProperty("mail.smtp.host", "smtp.example.com"));
    private static final String SMTP_PORT = System.getenv().getOrDefault("SMTP_PORT",
            props.getProperty("mail.smtp.port", "587"));
    private static final String EMAIL_USER = System.getenv().getOrDefault("EMAIL_USER",
            props.getProperty("mail.user", "user@example.com"));
    private static final String EMAIL_PASSWORD = System.getenv().getOrDefault("EMAIL_PASSWORD",
            props.getProperty("mail.password", "password"));
    private static final String RECIPIENT_EMAIL = System.getenv().getOrDefault("RECIPIENT_EMAIL",
            props.getProperty("mail.recipient", "admin@example.com"));

    // Slack Webhook URL
    private static final String SLACK_WEBHOOK_URL = System.getenv().getOrDefault("SLACK_WEBHOOK_URL",
            props.getProperty("slack.webhook.url", "https://hooks.slack.com/services/YOUR/WEBHOOK/URL"));

    public static void sendEmail(String subject, String body) {
        if ("smtp.example.com".equals(SMTP_HOST)) {
            logger.warn("Skipping email notification: SMTP_HOST is not configured.");
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USER, EMAIL_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USER));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(RECIPIENT_EMAIL));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            logger.info("Email notification sent to {}", RECIPIENT_EMAIL);

        } catch (MessagingException e) {
            logger.error("Failed to send email notification", e);
        }
    }

    public static void sendSlackMessage(String messageText) {
        if (SLACK_WEBHOOK_URL.contains("YOUR/WEBHOOK/URL")) {
            logger.warn("Skipping Slack notification: SLACK_WEBHOOK_URL is not customized.");
            return;
        }

        try {
            URL url = new URL(SLACK_WEBHOOK_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Simple JSON payload: {"text": "message"}
            // Escaping quotes and newlines for simple JSON
            String jsonPayload = "{\"text\": \"" + escapeJson(messageText) + "\"}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                logger.info("Slack notification sent.");
            } else {
                logger.error("Failed to send Slack notification. Response Code: {}", responseCode);
            }

        } catch (Exception e) {
            logger.error("Error sending Slack notification", e);
        }
    }

    private static String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");
    }
}
