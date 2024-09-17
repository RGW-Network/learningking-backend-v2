package com.byaffe.learningking.constants;

public enum TransactionType {
    COURSE_PAYMENT(0,"Deposit"),
    SUBSCRIPTION_PAYMENT(1,"Withdraw"),
    EVENT_PAYMENT(2,"Withdraw");


    private String uiName;
    private int id;

    TransactionType(int id, String name) {
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

    public static TransactionType getById(int id){
        for(TransactionType enumValue: TransactionType.values()){
            if(enumValue.id==id){
                return enumValue;
            }
        }
        return null;
    }
}
