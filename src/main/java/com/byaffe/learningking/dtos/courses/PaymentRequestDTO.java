package com.byaffe.learningking.dtos.courses;


import lombok.Data;

@Data
public class PaymentRequestDTO  {

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

    
}
