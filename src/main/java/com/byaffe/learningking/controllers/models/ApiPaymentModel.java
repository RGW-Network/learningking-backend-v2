package com.byaffe.learningking.controllers.models;

import org.byaffe.systems.api.ApiSecurity;

public class ApiPaymentModel extends ApiSecurity {

    private String transactionId;
    private String serieId;
    private String details;
    private String dateOfPayment;
    private float amount;
    private String advertCoverage;
    private int advertPeriod;
    private String raveId;
      private String planId;
       private String memberSubscriptionId;
    private String paymentType;
     private String phoneNumber;
    private String paymentFrequency;
    private String courseId;

    private String status;

    public String getMemberSubscriptionId() {
        return memberSubscriptionId;
    }

    public void setMemberSubscriptionId(String memberSubscriptionId) {
        this.memberSubscriptionId = memberSubscriptionId;
    }
    
    

    /**
     * @return the transactionId
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * @param transactionId the transactionId to set
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }


    /**
     * @return the details
     */
    public String getDetails() {
        return details;
    }

    public String getPaymentFrequency() {
        return paymentFrequency;
    }

    public void setPaymentFrequency(String paymentFrequency) {
        this.paymentFrequency = paymentFrequency;
    }

    /**
     * @param details the details to set
     */
    public void setDetails(String details) {
        this.details = details;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    /**
     * @return the dateOfPayment
     */
    public String getDateOfPayment() {
        return dateOfPayment;
    }

    /**
     * @param dateOfPayment the dateOfPayment to set
     */
    public void setDateOfPayment(String dateOfPayment) {
        this.dateOfPayment = dateOfPayment;
    }

    /**
     * @return the amount
     */
    public float getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(float amount) {
        this.amount = amount;
    }

    /**
     * @return the advertCoverage
     */
    public String getAdvertCoverage() {
        return advertCoverage;
    }

    /**
     * @param advertCoverage the advertCoverage to set
     */
    public void setAdvertCoverage(String advertCoverage) {
        this.advertCoverage = advertCoverage;
    }

    /**
     * @return the advertPeriod
     */
    public int getAdvertPeriod() {
        return advertPeriod;
    }

    /**
     * @param advertPeriod the advertPeriod to set
     */
    public void setAdvertPeriod(int advertPeriod) {
        this.advertPeriod = advertPeriod;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRaveId() {
        return raveId;
    }

    public void setRaveId(String raveId) {
        this.raveId = raveId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getSerieId() {
        return serieId;
    }

    public void setSerieId(String serieId) {
        this.serieId = serieId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    
    
    
}
