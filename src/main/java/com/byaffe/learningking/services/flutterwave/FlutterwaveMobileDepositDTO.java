package com.byaffe.learningking.services.flutterwave;

import lombok.Data;

@Data
public class FlutterwaveMobileDepositDTO {
    private String phone_number;
    private String network;
    private double amount;
    private String currency;
    private String email;
    private String tx_ref;



}
