package com.byaffe.learningking.shared.utils;

import com.byaffe.learningking.config.EnvironmentConstants;
import com.byaffe.learningking.models.SystemSetting;
import com.byaffe.learningking.services.SystemSettingService;
import com.google.gson.Gson;
import io.vertx.core.Vertx;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.MailMessage;
import io.vertx.ext.mail.StartTLSOptions;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @author RayGdhrt
 */

@Service
@Slf4j
public class VertxMailServiceImpl implements MailService {

    private static final Logger logger = LoggerFactory.getLogger(VertxMailServiceImpl.class);
    private static MailConfig MAIL_CONFIG;
    private static String FROM_ADDRESS;
    @Autowired
    private SystemSettingService settingService;

    private void initMailOptions() {
        if (MAIL_CONFIG != null) {
            log.info("Re-using existing mail settings...");
        } else {
            log.info("Initialising new mail settings...");
            SystemSetting systemSetting = settingService.getAppSetting();

            MAIL_CONFIG = new MailConfig();
            MAIL_CONFIG.setHostname(systemSetting.getSmtpHost());
            MAIL_CONFIG.setPort(Integer.parseInt(systemSetting.getSmtpPort()));
            MAIL_CONFIG.setUsername(systemSetting.getSmtpUsername());
            MAIL_CONFIG.setPassword(systemSetting.getSmtpPassword());
            MAIL_CONFIG.setStarttls(StartTLSOptions.REQUIRED);
            MAIL_CONFIG.setSsl(false);
            FROM_ADDRESS=systemSetting.getSmtpAddress();

            log.info("Mail Config>>>>>>>: " + new Gson().toJson(MAIL_CONFIG));
        }

    }

    public void sendEmail(String toAddress, String subject, String body, List<String> cc) {
        initMailOptions();
        MailClient mailClient = MailClient.create(Vertx.vertx(), MAIL_CONFIG);

        MailMessage message = new MailMessage();
        message.setFrom(FROM_ADDRESS);
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

    private MailSendResponse send(List<String> toAddresses, String subject, String body, List<String> cc) {

        try {
            initMailOptions();
            MailClient mailClient = MailClient.create(Vertx.vertx(), MAIL_CONFIG);

            MailMessage message = new MailMessage();
            message.setFrom(FROM_ADDRESS);
            message.setTo(toAddresses);
            message.setCc(cc);
            message.setBcc(System.getenv(EnvironmentConstants.DEFAULT_BCC_EMAIL));
            message.setSubject(subject);
            message.setHtml(body);
            AtomicBoolean success = new AtomicBoolean(false);
            mailClient.sendMail(message, result -> {
                if (result.succeeded()) {
                    System.out.println(result.result());
                    System.out.println("Mail sent");
                    success.set(true);
                } else {
                    System.out.println("got exception");
                    result.cause().printStackTrace();
                    success.set(false);
                }
            });
            return new MailSendResponse(success.get());
        } catch (Exception exception) {
            logger.warn(exception.getMessage());
            return new MailSendResponse(exception.getMessage(), false);
        }
    }


    public MailSendResponse sendEmail(String[] recievers, String subject, String bodyContent, String[] cc) throws IOException {
        //Create personalisation

        return send(Arrays.asList(recievers), subject, bodyContent, Arrays.asList(cc));
    }

    public MailSendResponse sendEmail(String[] recievers, String subject, String bodyContent) throws IOException {

        return send(Arrays.asList(recievers), subject, bodyContent, null);
    }

    public MailSendResponse sendEmail(String reciever, String subject, String bodyContent) {
        //Create personalisation
        return send(Arrays.asList(reciever), subject, bodyContent, null);
    }

}
