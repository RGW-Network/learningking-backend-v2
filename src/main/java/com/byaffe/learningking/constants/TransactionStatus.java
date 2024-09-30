package com.byaffe.learningking.constants;

import lombok.Getter;

@Getter
public enum TransactionStatus {
    PENDING(0,"Pending"),
    SUCCESSFUL(1,"Successful"),
    FAILED(2,"Failed"),
    CANCELED(3,"Cancelled"),
    INDETERMINATE(4,"Indeterminate"),
    PENDING_3DS_AUTHORISATION(5,"Pending 3DS Authorisation"),
    PENDING_AVS_AUTHORISATION(6,"Pending AVS Authorisation"),
    PENDING_PIN_AUTHORISATION(7,"Pending Pin Authorisation"),
    PENDING_OTP_VALIDATION(8,"Pending OTP Validation"),
    FAILED_AUTHORISATION(9,"Failed Authorization"),;
    ;
    ;
    private String uiName;
    private int id;

    TransactionStatus(int id, String name) {
        this.uiName = name;
        this.id=id;
    }

    public void setUiName(String uiName) {
        this.uiName = uiName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static TransactionStatus getById(int id){
        for(TransactionStatus enumValue: TransactionStatus.values()){
            if(enumValue.id==id){
                return enumValue;
            }
        }
        return null;
    }
}
