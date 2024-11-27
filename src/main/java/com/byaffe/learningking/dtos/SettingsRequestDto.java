package com.byaffe.learningking.dtos;

import com.byaffe.learningking.models.payments.Currency;
import com.byaffe.learningking.shared.api.BaseDTO;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
public class SettingsRequestDto {
    private String logoUrl;
    private Currency baseCurrency;
    private String smsApiUsername;
    private String smsApiPassword;
    private String smtpAddress = "";
    private String smtpPassword = "";
    private String smtpHost = "";
    private String smtpPort = "587";
    private String smtpUsername = "587";
    private String defaultTrainingMandate;
}
