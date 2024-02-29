/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.byaffe.learningking.models.payments;

import java.util.Date;

/**
 *
 * @author HP
 */
public class FlutterTransactionRequest {

    public String status;
    public String message;
    public MainDataLoad data;

    public class CardDetails {

        public String first_6digits;
        public String last_4digits;
        public String issuer;
        public String country;
        public String type;
        public String token;
        public String expiry;
    }

    public class CustomerDetails {

        public int id;
        public String name;
        public String phone_number;
        public String email;
        public Date created_at;

        @Override
        public String toString() {
            return "Customer{" + "id=" + id + ", name=" + name + ", phone_number=" + phone_number + ", email=" + email + ", created_at=" + created_at + '}';
        }
        
        
    }

    public class MainDataLoad {

        public int id;
        public String tx_ref;
        public String flw_ref;
        public String device_fingerprint;
        public int amount;
        public String currency;
        public int charged_amount;
        public int app_fee;
        public int merchant_fee;
        public String processor_response;
        public String auth_model;
        public String ip;
        public String narration;
        public String status;
        public String payment_type;
        public Date created_at;
        public int account_id;
        public int amount_settled;
        public CardDetails card;
        public CustomerDetails customer;

        @Override
        public String toString() {
            return "Data{" + "id=" + id + ", tx_ref=" + tx_ref + ", flw_ref=" + flw_ref + ", device_fingerprint=" + device_fingerprint + ", amount=" + amount + ", currency=" + currency + ", charged_amount=" + charged_amount + ", app_fee=" + app_fee + ", merchant_fee=" + merchant_fee + ", processor_response=" + processor_response + ", auth_model=" + auth_model + ", ip=" + ip + ", narration=" + narration + ", status=" + status + ", payment_type=" + payment_type + ", created_at=" + created_at + ", account_id=" + account_id + ", amount_settled=" + amount_settled + ", card=" + card + ", customer=" + customer + '}';
        }
        
        
    }

    @Override
    public String toString() {
        return "FlutterMMRequest{" + "status=" + status + ", message=" + message + ", data=" + data + '}';
    }

    
    
}
