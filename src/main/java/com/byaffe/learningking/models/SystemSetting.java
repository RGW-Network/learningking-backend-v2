package com.byaffe.learningking.models;

import com.byaffe.learningking.models.payments.Currency;
import com.byaffe.learningking.shared.models.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "app_setting")
@Inheritance(strategy = InheritanceType.JOINED)
public class SystemSetting extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private float referrerPercentageCharge;
    private String systemLogoUrl;
    private Currency baseCurrency;
    
    private String learningKingBaseUrl;
    private String learningKingApiUsername;
    private String learningKingApiPassword;
    
    
    private String egosmsUrl;
    private String egoSmsApiUsername;
    private String egoSmsApiPassword;
    
    private String smtpAddress = "";
    private String smtpPassword = "";
    private String smtpHost = "";
    private String smtpPort = "587";
      private String smtpUsername = "587";
      
    private String balanceCode = "UGX-MTNMM";
    private String flutterwavePublicKey = "";
    private String flutterwaveEncryptionKey = "";
    private String flutterwaveSecretKey = "";
    private String flutterwaveUrl = "https://api.flutterwave.com/v3/";
     private String flutterwaveReditectUrl = "http://212.111.42.51:8080/aapu/PaymentComplete";
     

    /**
     * @return the referrerPercentageCharge
     */
    @Column(name = "referrer_percentage_charge", nullable = true, length = 10)
    public float getReferrerPercentageCharge() {
        return referrerPercentageCharge;
    }

    /**
     * @param referrerPercentageCharge the referrerPercentageCharge to set
     */
    public void setReferrerPercentageCharge(float referrerPercentageCharge) {
        this.referrerPercentageCharge = referrerPercentageCharge;
    }
    @Column(name = "smtp_username", nullable = true, length = 100)
    public String getSmtpUsername() {
        return smtpUsername;
    }

    public void setSmtpUsername(String smtpUsername) {
        this.smtpUsername = smtpUsername;
    }

    /**
     * @return the egosmsUrl
     */
    @Column(name = "egosms_url", nullable = true, length = 100)
    public String getEgosmsUrl() {
        return egosmsUrl;
    }

        @Column(name = "system_logo_url", nullable = true, length = 500)
    public String getSystemLogoUrl() {
        return systemLogoUrl;
    }

    public void setSystemLogoUrl(String systemLogoUrl) {
        this.systemLogoUrl = systemLogoUrl;
    }

    /**
     * @param egosmsUrl the egosmsUrl to set
     */
    public void setEgosmsUrl(String egosmsUrl) {
        this.egosmsUrl = egosmsUrl;
    }

    /**
     * @return the egoSmsApiUsername
     */
    @Column(name = "ego_sms_api_username", nullable = true, length = 100)
    public String getEgoSmsApiUsername() {
        return egoSmsApiUsername;
    }

    @ManyToOne
    @JoinColumn(name = "base_currency")
    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(Currency baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    /**
     * @param egoSmsApiUsername the egoSmsApiUsername to set
     */
    public void setEgoSmsApiUsername(String egoSmsApiUsername) {
        this.egoSmsApiUsername = egoSmsApiUsername;
    }

    /**
     * @return the egoSmsApiPassword
     */
    @Column(name = "ego_sms_api_password", nullable = true, length = 100)
    public String getEgoSmsApiPassword() {
        return egoSmsApiPassword;
    }

    /**
     * @param egoSmsApiPassword the egoSmsApiPassword to set
     */
    public void setEgoSmsApiPassword(String egoSmsApiPassword) {
        this.egoSmsApiPassword = egoSmsApiPassword;
    }

    @Column(name = "balanca_code", length = 10)
    public String getBalanceCode() {
        return balanceCode;
    }

    public void setBalanceCode(String balanceCode) {
        this.balanceCode = balanceCode;
    }


    @Column(name = "smtp_address")
    public String getSmtpAddress() {
        return smtpAddress;
    }

    public void setSmtpAddress(String smtpAddress) {
        this.smtpAddress = smtpAddress;
    }

    @Column(name = "smtp_password")
    public String getSmtpPassword() {
        return smtpPassword;
    }

    public void setSmtpPassword(String smtpPassword) {
        this.smtpPassword = smtpPassword;
    }

    @Column(name = "smtp_host")
    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    @Column(name = "smtp_port")
    public String getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

  
     @Column(name = "rave_public_key")
    public String getFlutterwavePublicKey() {
        return flutterwavePublicKey;
    }

    public void setFlutterwavePublicKey(String flutterwavePublicKey) {
        this.flutterwavePublicKey = flutterwavePublicKey;
    }

      @Column(name = "rave_encryption_key")
    public String getFlutterwaveEncryptionKey() {
        return flutterwaveEncryptionKey;
    }

    public void setFlutterwaveEncryptionKey(String flutterwaveEncryptionKey) {
        this.flutterwaveEncryptionKey = flutterwaveEncryptionKey;
    }

      @Column(name = "rave_secret_key")
    public String getFlutterwaveSecretKey() {
        return flutterwaveSecretKey;
    }

    public void setFlutterwaveSecretKey(String flutterwaveSecretKey) {
        this.flutterwaveSecretKey = flutterwaveSecretKey;
    }

      @Column(name = "rave_base_url")
    public String getFlutterwaveUrl() {
        return flutterwaveUrl;
    }

    public void setFlutterwaveUrl(String flutterwaveUrl) {
        this.flutterwaveUrl = flutterwaveUrl;
    }

    @Column(name = "rave_redirect_url")
    public String getFlutterwaveReditectUrl() {
        return flutterwaveReditectUrl;
    }

    public void setFlutterwaveReditectUrl(String flutterwaveReditectUrl) {
        this.flutterwaveReditectUrl = flutterwaveReditectUrl;
    }

     @Column(name = "learningking_base_url")
    public String getLearningKingBaseUrl() {
        return learningKingBaseUrl;
    }

    public void setLearningKingBaseUrl(String learningKingBaseUrl) {
        this.learningKingBaseUrl = learningKingBaseUrl;
    }

      @Column(name = "learningking_api_username")
    public String getLearningKingApiUsername() {
        return learningKingApiUsername;
    }

    public void setLearningKingApiUsername(String learningKingApiUsername) {
        this.learningKingApiUsername = learningKingApiUsername;
    }

     @Column(name = "learningking_api_password")
    public String getLearningKingApiPassword() {
        return learningKingApiPassword;
    }

    public void setLearningKingApiPassword(String learningKingApiPassword) {
        this.learningKingApiPassword = learningKingApiPassword;
    }

    

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
