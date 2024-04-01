/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.byaffe.learningking.services.flutterwave;

import com.byaffe.learningking.models.payments.PaymentPrefixes;
import com.byaffe.learningking.utilities.AppUtils;


/**
 *
 * @author User
 */
public class TransactionIdGenerator {

  public  static final int TRANSACTION_ID_LENGHT = 10;

    public static String generateDonationId() {
        return PaymentPrefixes.DONATION_PREFIX + AppUtils.getRandomString(TRANSACTION_ID_LENGHT);
    }
    
    public static String generateCoursePaymentId() {
        return PaymentPrefixes.COURSE_PAYMENT_PREFIX + AppUtils.getRandomString(TRANSACTION_ID_LENGHT);
    }
    
 

}
