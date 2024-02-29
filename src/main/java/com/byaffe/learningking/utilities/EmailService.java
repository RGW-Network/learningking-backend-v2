package com.byaffe.learningking.utilities;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.byaffe.systems.core.services.SystemSettingService;
import org.byaffe.systems.core.utilities.HtmlMailTemplate;
import org.byaffe.systems.models.SystemSetting;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.CustomLogger;

public class EmailService {

    private static Properties prop;

    private static SystemSetting systemSetting;
    private static Session session;

    public static void initMailService() {
        CustomLogger.log(EmailService.class, "Initialising mail session...");

        if (systemSetting == null) {
            systemSetting = ApplicationContextProvider.getBean(SystemSettingService.class).getAppSetting();
        }
        if (session == null) {
            CustomLogger.log(EmailService.class, "Initialising mail session...");

            prop = new Properties();
            prop.put("mail.smtp.auth", true);
            prop.put("mail.smtp.starttls.enable", "true");
            prop.put("mail.smtp.host", systemSetting.getSmtpHost());
            prop.put("mail.smtp.port", systemSetting.getSmtpPort());
            prop.put("mail.smtp.ssl.trust", systemSetting.getSmtpHost());
            prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
            session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(systemSetting.getSmtpUsername(), systemSetting.getSmtpPassword());
                }
            });

        }
    }

    public static void main(String... args) {
        try {

            CustomLogger.log(EmailService.class, "Initialising mail session...");
            prop = new Properties();
            prop.put("mail.smtp.auth", true);
            prop.put("mail.smtp.starttls.enable", "true");
            prop.put("mail.smtp.host", "email-smtp.eu-north-1.amazonaws.com");
            prop.put("mail.smtp.port", 587);
            prop.put("mail.smtp.ssl.trust", true);
            prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
            session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("AKIA3MQ36TZ5NVGMZQOR",
                            "BMUFiXFAURkyXZLtMS3nMrJ3vHdJJf++NXlm6aSLBdfy");
                }
            });

            systemSetting = new SystemSetting();
            systemSetting.setSmtpAddress("mail@revivalgateway.org");
            sendMail("mutebiraymond695@gmail.com", "TEST EMAIL", "This is my <b>Test</b> email");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMail(String toAddress, String subject, String body) throws Exception {
        initMailService();
        CustomLogger.log(EmailService.class, "Sending email...");

        send(session,toAddress,  subject, body);
    }

    private static void send(Session session,String toAddress, String subject, String body) throws Exception {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(systemSetting.getSmtpAddress()));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
        message.setSubject(subject);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(HtmlMailTemplate.buildTeplatedEmail(subject, body), "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);
        message.writeTo(System.out);
        Transport.send(message, systemSetting.getSmtpUsername(), systemSetting.getSmtpPassword());
    }

    public static void sendMails(List<String> toAddresses, String subject, String body, String cc) {
        initMailService();
        CustomLogger.log(EmailService.class, "Sending emails...");

        for (String toAddress : toAddresses) {
            try {
                send(session,toAddress,  subject, body);
            } catch (Exception ex) {
                Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    private File getFile() throws Exception {
        URI uri = this.getClass()
                .getClassLoader()
                .getResource("attachment.txt")
                .toURI();
        return new File(uri);
    }

}
