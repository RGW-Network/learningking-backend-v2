package com.byaffe.learningking.models;

import java.util.stream.Stream;

public enum CurrencyEnum {
    USD(1,"US Dollar","us","USD" ),
    UGX(2,"Uganda Shillings","ug","UGX"),
    KSH(3,"Kenya Shillings","ke","KSH"),
    TSH(4,"Tanzania Shillings","tz","TSH"),
    RIYAL(5,"Saudi Riyal","sar","SAR");

    ;

    private String uiName;
    private String countryCode;
    private String symbol;
    private String pgwCode;
    private int id;
    private double dollarRate;
    CurrencyEnum(int id, String name, String countryCode, String symbol) {
        this.uiName = name;
        this.id=id;
        this.countryCode=countryCode;
        this.symbol=symbol;
        this.pgwCode=symbol;
        this.dollarRate=dollarRate;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setPgwCode(String pgwCode) {
        this.pgwCode = pgwCode;
    }

    public String getUiName() {
        return uiName;
    }

    public void setUiName(String uiName) {
        this.uiName = uiName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public void setDollarRate(double dollarRate) {
        this.dollarRate = dollarRate;
    }
    public static CurrencyEnum resolveCurrency(String phoneNumber) {

        if (phoneNumber == null) {
            return  USD;
        }else
        if (Stream.of("+256").anyMatch(phoneNumber::startsWith)) {
            return UGX;
        }else
        if (Stream.of("+254").anyMatch(phoneNumber::startsWith)) {
            return KSH;
        }else
        if (Stream.of("+233").anyMatch(phoneNumber::startsWith)) {
            return TSH;
        }else
        if (Stream.of("+247").anyMatch(phoneNumber::startsWith)) {
            return UGX;
        }
        return USD;
    }
    public static CurrencyEnum getById(int id){
        for(CurrencyEnum enumValue: CurrencyEnum.values()){
            if(enumValue.id==id){
                return enumValue;
            }
        }
        return null;
    }

    public String getPgwCode() {
        return  pgwCode;
    }
}
