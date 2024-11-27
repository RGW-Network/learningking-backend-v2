package com.byaffe.learningking.models;

import com.byaffe.learningking.models.payments.Currency;
import com.byaffe.learningking.shared.models.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "app_settings")
@Inheritance(strategy = InheritanceType.JOINED)
public class SystemSetting extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "logo_url")
     private String logoUrl;

    @ManyToOne
    @JoinColumn(name = "base_currency_id")
    private Currency baseCurrency;

    @Column(name = "sms_api_username")
    private String smsApiUsername;
    @Column(name = "sms_api_password")
    private String smsApiPassword;

    @Column(name = "smtp_address")
    private String smtpAddress = "";
    @Column(name = "smtp_password")
    private String smtpPassword = "";
    @Column(name = "smtp_host")
    private String smtpHost = "";
    @Column(name = "smtp_port")
    private String smtpPort = "587";
    @Column(name = "smtp_username")
    private String smtpUsername = "587";

    @Column(name = "default_training_mandate", columnDefinition = "TEXT")
    private String defaultTrainingMandate;


    @Override
    public boolean equals(Object object) {
        return object instanceof SystemSetting && (super.getId() != null) ? super.getId().equals(((SystemSetting) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
