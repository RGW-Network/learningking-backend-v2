package com.byaffe.learningking.models.payments;

import com.byaffe.learningking.constants.TransactionStatus;
import com.byaffe.learningking.shared.models.BaseEntity;

import javax.persistence.*;

@MappedSuperclass
public class BasePayment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private TransactionStatus status = TransactionStatus.NEW_CREATED;
    private PaymentType paymentType = PaymentType.MOBILE_MONEY;
    protected String transactionId;
    private String raveId;
    private String lastRavePaymentLink;
    private String phoneNumber;
    private String title = "LK payment";
    private Currency currency;
    private String description = "LK payment";
    private String lastPgwResponse;
     private String failureReason="";
    private float amount = 0.0f;

    /**
     * @return the status
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 1000)
    public TransactionStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    /**
     * @return the errorMessage
     */
    @Column(name = "last_pgw_response", nullable = true,columnDefinition = "TEXT")

    public String getLastPgwResponse() {
        return lastPgwResponse;
    }

    public void setLastPgwResponse(String lastPgwResponse) {
        this.lastPgwResponse = lastPgwResponse;
    }

     @Column(name = "failure_reason", nullable = true,columnDefinition = "TEXT")

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }


    @Column(name = "transaction_id",  nullable = true, length = 100)
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Column(name = "amount", nullable = true, length = 100)
    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

  

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = true)
    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    @Column(name = "rave_id")
    public String getRaveId() {
        return raveId;
    }

    public void setRaveId(String raveId) {
        this.raveId = raveId;
    }

    @Column(name = "phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne
    @JoinColumn(name = "currency_id")
    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Column(name = "last_rave_payment_link")
    public String getLastRavePaymentLink() {
        return lastRavePaymentLink;
    }

    public void setLastRavePaymentLink(String lastRavePaymentLink) {
        this.lastRavePaymentLink = lastRavePaymentLink;
    }

    public String attachTransactionId() {
        this.transactionId = String.valueOf(this.id);
        return this.transactionId;
    }

}
