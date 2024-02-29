/*
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.byaffe.learningking.models;

import java.util.Date;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.byaffe.learningking.constants.CommunicationType;
import com.byaffe.learningking.shared.models.BaseEntity;

/**
 *
 * @author Ray Gdhrt
 */
@Entity
@Table(name = "communications")
public class Communication extends BaseEntity {

    private String emailSubject;
    private String emailBody;
    private String smsMessage;
    private List<String> recipients;
    private CommunicationType communicationType;
    private boolean emailsSent=false;
    private boolean smsSent=false;
    private Date scheduleDate;
     private Date scheduleTime;
    private int sentEmails;
    private int sentPhones;

   
    @Column(name = "email_subject")
    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    @Column(name = "email_body", length = 1000)
    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    @Column(name = "sms_message")
    public String getSmsMessage() {
        return smsMessage;
    }

    public void setSmsMessage(String smsMessage) {
        this.smsMessage = smsMessage;
    }

     @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "recipients")
    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipientPhones) {
        this.recipients = recipientPhones;
    }

    

    @Enumerated(EnumType.STRING)
    @Column(name = "channel")
    public CommunicationType getCommunicationType() {
        return communicationType;
    }

    public void setCommunicationType(CommunicationType communicationType) {
        this.communicationType = communicationType;
    }

    @Column(name = "email_sent")
    public boolean isEmailsSent() {
        return emailsSent;
    }

    public void setEmailsSent(boolean emailsSent) {
        this.emailsSent = emailsSent;
    }

     @Column(name = "sms_sent")
    public boolean isSmsSent() {
        return smsSent;
    }

    public void setSmsSent(boolean smsSent) {
        this.smsSent = smsSent;
    }

    @Temporal(TemporalType.DATE)
     @Column(name = "schedule_date")
    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

     @Temporal(TemporalType.TIME)
     @Column(name = "schedule_time")
    public Date getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(Date scheduleTime) {
        this.scheduleTime = scheduleTime;
    }
    
    

     @Column(name = "sent_emails")
    public int getSentEmails() {
        return sentEmails;
    }

    public void setSentEmails(int sentEmails) {
        this.sentEmails = sentEmails;
    }

     @Column(name = "sent_sms")
    public int getSentPhones() {
        return sentPhones;
    }

    public void setSentPhones(int sentPhones) {
        this.sentPhones = sentPhones;
    }

}
