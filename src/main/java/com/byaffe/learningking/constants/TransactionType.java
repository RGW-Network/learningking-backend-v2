package com.byaffe.learningking.constants;

public enum TransactionType {
    COURSE_PAYMENT(0,"Course Payment"),
    SUBSCRIPTION_PAYMENT(1,"Subscription Payment"),
    EVENT_PAYMENT(2,"Event Payment"),
    BULK_COURSE_PAYMENT(3,"Bulk Course Payment"),
    BULK_SUBSCRIPTION_PAYMENT(4,"Bulk Subscription Payment"),
    BULK_EVENT_PAYMENT(5,"Bulk Event Payment");


    private String uiName;
    private int id;

    TransactionType(int id, String name) {
        this.uiName = name;
        this.id=id;
    }

    public String getDisplayName() {
        return uiName;
    }

    public int getId() {
        return id;
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
