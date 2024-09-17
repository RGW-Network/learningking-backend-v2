package com.byaffe.learningking.services.flutterwave;

import lombok.Data;

@Data
public class FlutterWaveStandardDepositRequestDto {
    private String tx_ref;
    private String amount;
    private String currency;
    private String redirect_url="#";
    private String payment_options="card,mobile_money_uganda,mpesa,mobile_money_tanzania";
    private CustomerDTO customer;
    private CustomizationsDTO customizations;
}

@Data
class CustomerDTO {
    private String email;
    private String name;
    private String phonenumber;
}

@Data
class CustomizationsDTO {
    private String title;
    private String logo="https://play-lh.googleusercontent.com/RY0_BgsLkC4WfJm-R4KF24PRQnmuo7rEUVSC9XrC48pBKEKlFJBdFQGWeQnI0o3_cbE";

}
