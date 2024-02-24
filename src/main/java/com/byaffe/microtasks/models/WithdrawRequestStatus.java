package com.byaffe.microtasks.models;

public enum WithdrawRequestStatus {
    DRAFT(0,"Draft"),
    SUBMITTED(1,"Submitted"),
    DISBURSED(2,"Disbursed"),
    REJECTED(-1,"Rejected"),
    CANCELLED(-2,"Cancelled");
    private String uiName;
    private int id;

    WithdrawRequestStatus(int id, String name) {
        this.uiName = name;
        this.id=id;
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

    public static WithdrawRequestStatus getById(int id){
        for(WithdrawRequestStatus enumValue: WithdrawRequestStatus.values()){
            if(enumValue.id==id){
                return enumValue;
            }
        }
        return null;
    }
}
