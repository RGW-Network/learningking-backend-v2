/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.byaffe.learningking.services.flutterwave;

import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.SystemSetting;
import com.byaffe.learningking.models.payments.BasePayment;
import com.byaffe.learningking.services.SystemSettingService;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.byaffe.learningking.utilities.SSLUtilities;
import com.flutterwave.rave.java.entry.transValidation;
import com.flutterwave.rave.java.payload.transverifyPayload;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.io.IOException;

/**
 *
 * @author RayGdhrt
 */
public class FlutterwaveClient {

    public static final String FLUTTERWAVE_PAYMENT_TYPES = "card,mobilemoney";

    public static SystemSetting systemSetting;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    public void initSettings() {

        systemSetting = ApplicationContextProvider.getBean(SystemSettingService.class).getAppSetting();


    }

    public <T extends BasePayment> FlutterReponse requestPaymentInitiation(T payment, Student student) throws IOException {
        initSettings();
        FluterwaveRequest fluterwaveRequest = new FluterwaveRequest()
                .addAmount(String.valueOf(payment.getAmount()))
                .addCurrency(payment.getCurrency().getPgwCode())
                .addTxRef(payment.getTransactionId())
                .addPaymentOptions(FLUTTERWAVE_PAYMENT_TYPES)
                .addRedirectUrl(systemSetting.getFlutterwaveReditectUrl())
                .addCustomer(new Customer()
                        .addEmail(student.getUserAccount().getEmailAddress())
                        .addPhonenumber(student.getUserAccount().getPhoneNumber())
                        .addName(student.getFullName()))
                .addCustomizations(new Customizations()
                        .addLogo(systemSetting.getSystemLogoUrl())
                        .addTitle(payment.getTitle())
                        .addDescription(payment.getDescription()));

        SSLUtilities.trustAllHostnames();
        SSLUtilities.trustAllHttpsCertificates();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        OkHttpClient client = builder.build();
        RequestBody body = RequestBody.create(JSON, new Gson().toJson(fluterwaveRequest));
        Request request = new Request.Builder()
                .url(systemSetting.getFlutterwaveUrl() + "payments/")
                .get()
                .post(body)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + systemSetting.getFlutterwaveSecretKey())
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        String stringResponse = response.body().string();

        FlutterReponse flutterReponse = new Gson().fromJson(stringResponse, FlutterReponse.class);

        return flutterReponse;
    }

    public void buildPaymentJsonObject() {

    }

    public FlutterReponse checkPaymentStatusV2(String transactionId) throws IOException {
        initSettings();
        transValidation transValidation = new transValidation();
        transverifyPayload transverifyPayload = new transverifyPayload();
        transverifyPayload.setSECKEY(systemSetting.getFlutterwaveSecretKey());
        transverifyPayload.setTxref(transactionId);

        String response = transValidation.bvnvalidate(transverifyPayload);
        FlutterReponse flutterReponse = new Gson().fromJson(response, FlutterReponse.class);

        return flutterReponse;
    }

    public FlutterReponse checkPaymentStatusByTransactionId(String transactionId) throws IOException {
        initSettings();
        SSLUtilities.trustAllHostnames();
        SSLUtilities.trustAllHttpsCertificates();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        OkHttpClient client = builder.build();
        Request request = new Request.Builder()
                .url(systemSetting.getFlutterwaveUrl() + "transactions/verify_by_reference?tx_ref=" + transactionId)
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + systemSetting.getFlutterwaveSecretKey())
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        Gson gSon = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        String stringResponse = response.body().string();

        FlutterReponse flutterReponse = gSon.fromJson(stringResponse, FlutterReponse.class);

        return flutterReponse;
    }

    public void main(String[] args) {
        //new FlutterwaveClient().requestPaymentInitiation("shdbcsdhbchjscd", 0, "WYSBUHISIXIXS", FLUTTER_SECRET_KEY, FLUTTER_SECRET_KEY)
    }
}
