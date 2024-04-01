package com.byaffe.learningking.services.flutterwave;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;




@Generated("jsonschema2pojo")
public class FluterwaveRequest {

    @SerializedName("tx_ref")
    @Expose
    private String txRef;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("redirect_url")
    @Expose
    private String redirectUrl;
    @SerializedName("payment_options")
    @Expose
    private String paymentOptions;
    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("customer")
    @Expose
    private Customer customer;
    @SerializedName("customizations")
    @Expose
    private Customizations customizations;

    public String getTxRef() {
        return txRef;
    }

    public void setTxRef(String txRef) {
        this.txRef = txRef;
    }

    public FluterwaveRequest addTxRef(String txRef) {
        this.txRef = txRef;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public FluterwaveRequest addAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public FluterwaveRequest addCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public FluterwaveRequest addRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
        return this;
    }

    public String getPaymentOptions() {
        return paymentOptions;
    }

    public void setPaymentOptions(String paymentOptions) {
        this.paymentOptions = paymentOptions;
    }

    public FluterwaveRequest addPaymentOptions(String paymentOptions) {
        this.paymentOptions = paymentOptions;
        return this;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public FluterwaveRequest addMeta(Meta meta) {
        this.meta = meta;
        return this;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public FluterwaveRequest addCustomer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public Customizations getCustomizations() {
        return customizations;
    }

    public void setCustomizations(Customizations customizations) {
        this.customizations = customizations;
    }

    public FluterwaveRequest addCustomizations(Customizations customizations) {
        this.customizations = customizations;
        return this;
    }

}

@Generated("jsonschema2pojo")
class Customer {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phonenumber")
    @Expose
    private String phonenumber;
    @SerializedName("name")
    @Expose
    private String name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Customer addEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public Customer addPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Customer addName(String name) {
        this.name = name;
        return this;
    }

}

@Generated("jsonschema2pojo")
class Customizations {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("logo")
    @Expose
    private String logo;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Customizations addTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Customizations addDescription(String description) {
        this.description = description;
        return this;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Customizations addLogo(String logo) {
        this.logo = logo;
        return this;
    }

}

@Generated("jsonschema2pojo")
class Meta {

    @SerializedName("consumer_id")
    @Expose
    private Integer consumerId;
    @SerializedName("consumer_mac")
    @Expose
    private String consumerMac;

    public Integer getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(Integer consumerId) {
        this.consumerId = consumerId;
    }

    public Meta addConsumerId(Integer consumerId) {
        this.consumerId = consumerId;
        return this;
    }

    public String getConsumerMac() {
        return consumerMac;
    }

    public void setConsumerMac(String consumerMac) {
        this.consumerMac = consumerMac;
    }

    public Meta addConsumerMac(String consumerMac) {
        this.consumerMac = consumerMac;
        return this;
    }

}
