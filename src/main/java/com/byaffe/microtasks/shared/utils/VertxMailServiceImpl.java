package com.byaffe.microtasks.shared.utils;

import com.byaffe.microtasks.config.EnvironmentConstants;
import io.vertx.core.Vertx;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.MailMessage;
import io.vertx.ext.mail.StartTLSOptions;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


/**
 * @author RayGdhrt
 */

@Service
@Slf4j
public class VertxMailServiceImpl implements MailService{

    private static final Logger logger = LoggerFactory.getLogger(VertxMailServiceImpl.class);
    private static MailConfig MAIL_CONFIG;

    static void initMailOptions() {

        if (MAIL_CONFIG == null) {
            log.info("Initialising mail settings...");
            MAIL_CONFIG = new MailConfig();
            MAIL_CONFIG.setHostname("smtp.gmail.com");
            MAIL_CONFIG.setPort(587);
            MAIL_CONFIG.setStarttls(StartTLSOptions.REQUIRED);
            MAIL_CONFIG.setUsername(System.getenv(EnvironmentConstants.MAIL_SENDER_EMAIL));
            MAIL_CONFIG.setPassword(System.getenv(EnvironmentConstants.MAIL_SENDER_PASS_KEY));
        }

    }

    public static void sendEmail(String toAddress, String subject, String body, List<String> cc) {
        initMailOptions();
        MailClient mailClient = MailClient.create(Vertx.vertx(), MAIL_CONFIG);

        MailMessage message = new MailMessage();
        message.setFrom(System.getenv(EnvironmentConstants.MAIL_SENDER_EMAIL));
        message.setTo(toAddress);
        message.setCc(cc);
        message.setSubject(subject);
        message.setHtml(body);

        mailClient.sendMail(message, result -> {
            if (result.succeeded()) {
                System.out.println(result.result());
                System.out.println("Mail sent");
            } else {
                System.out.println("got exception");
                result.cause().printStackTrace();
            }
        });

    }

    private  MailSendResponse send(List<String> toAddresses, String subject, String body, List<String> cc) {

        try {
            initMailOptions();
            MailClient mailClient = MailClient.create(Vertx.vertx(), MAIL_CONFIG);

            MailMessage message = new MailMessage();
            message.setFrom(System.getenv(EnvironmentConstants.MAIL_SENDER_EMAIL));
            message.setTo(toAddresses);
            message.setCc(cc);
            message.setBcc(System.getenv(EnvironmentConstants.DEFAULT_BCC_EMAIL));
            message.setSubject(subject);
            message.setHtml(EmailTemplate.buildTeplatedEmail(subject, body));

            mailClient.sendMail(message, result -> {
                if (result.succeeded()) {
                    System.out.println(result.result());
                    System.out.println("Mail sent");

                } else {
                    System.out.println("got exception");
                    result.cause().printStackTrace();
                }
            });
            return new MailSendResponse(true);
        }catch (Exception exception){
            logger.warn(exception.getMessage());
            return new MailSendResponse(exception.getMessage(),false);
        }
    }

    public static void main(String[] args) {

        //Testing it out
        sendEmail("mutebiraymond695@gmail.com","DALL·E 2 is a new AI system ", EmailTemplate.buildTeplatedEmail("Success email sent", "<h4>DALL·E 2 is a new AI system that can create realistic images and art from a description in natural language.</h4>"), null);
    }


    public MailSendResponse sendEmail(String[] recievers, String subject, String bodyContent, String[] cc) throws IOException {
        //Create personalisation

        return send(Arrays.asList(recievers),subject,bodyContent,Arrays.asList(cc));
    }

    public MailSendResponse sendEmail(String[] recievers, String subject, String bodyContent) throws IOException {

        return send(Arrays.asList(recievers),subject,bodyContent,null);
    }
    public MailSendResponse sendEmail(String reciever, String subject, String bodyContent) {
        //Create personalisation
       return send(Arrays.asList(reciever),subject,bodyContent,null);
    }

}
