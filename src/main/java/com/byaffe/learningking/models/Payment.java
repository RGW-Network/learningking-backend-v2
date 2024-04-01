package com.byaffe.learningking.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.byaffe.learningking.constants.TransactionStatus;
import com.byaffe.learningking.shared.models.BaseEntity;

/**
 *
 * @author Ray Gdhrt
 */
@Entity
@Table(name = "payments")
public class Payment extends BaseEntity {

    private String phoneNumber;
    private String description;
    private String transactionId;
    private float amount;
    private String trasanctionType;
    private float totalCharge;
    private float serviceCharge;
    private float percentageCharge;
    private String raveId;
    private TransactionStatus trasanctionStatus;
    private String referenceNumber;
    private String trasanctionDate;
    private String message;
    private int status;

    private Student student;
    private PaymentReasonType reasonType;

    @Column(name = "phoneNumber")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(name = "description", columnDefinition = "TEXT")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "amount")
    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Column(name = "transaction_type")
    public String getTrasanctionType() {
        return trasanctionType;
    }

    public void setTrasanctionType(String trasanctionType) {
        this.trasanctionType = trasanctionType;
    }

    @Column(name = "total_charge", length = 10)
    public float getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(float totalCharge) {
        this.totalCharge = totalCharge;
    }

    @Column(name = "service_charge", length = 10)
    public float getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(float serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    @Column(name = "percentage_charge", length = 10)
    public float getPercentageCharge() {
        return percentageCharge;
    }

    public void setPercentageCharge(float percentageCharge) {
        this.percentageCharge = percentageCharge;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status")
    public TransactionStatus getTrasanctionStatus() {
        return trasanctionStatus;
    }

    public void setTrasanctionStatus(TransactionStatus trasanctionStatus) {
        this.trasanctionStatus = trasanctionStatus;
    }

    @Column(name = "reference_number")
    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    //@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "transaction_date")
    public String getTrasanctionDate() {
        return trasanctionDate;
    }

    public void setTrasanctionDate(String trasanctionDate) {
        this.trasanctionDate = trasanctionDate;
    }

    @Column(name = "message", columnDefinition = "TEXT")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Column(name = "status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @ManyToOne
    @JoinColumn(name = "member_id")
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_reason_type")
    public PaymentReasonType getReasonType() {
        return reasonType;
    }

    public void setReasonType(PaymentReasonType reasonType) {
        this.reasonType = reasonType;
    }

    @Column(name = "transaction_id", unique = true, nullable = false)
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Column(name = "rave_id", nullable = true)
    public String getRaveId() {
        return raveId;
    }

    public void setRaveId(String raveId) {
        this.raveId = raveId;
    }

    @Override
    public String toString() {
        return "PaymentLog{" + "phoneNumber=" + phoneNumber + ", description=" + description + ", amount=" + amount + '}';
    }

}
