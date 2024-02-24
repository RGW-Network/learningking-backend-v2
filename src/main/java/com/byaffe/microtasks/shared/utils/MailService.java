package com.byaffe.microtasks.shared.utils;


import java.io.IOException;

/**
 * @author RayGdhrt
 */


public interface MailService {

    public MailSendResponse sendEmail(String[] recievers, String subject, String bodyContent, String[] cc) throws IOException ;

    public MailSendResponse sendEmail(String[] recievers, String subject, String bodyContent) throws IOException;
    public MailSendResponse sendEmail(String reciever, String subject, String bodyContent);

}
