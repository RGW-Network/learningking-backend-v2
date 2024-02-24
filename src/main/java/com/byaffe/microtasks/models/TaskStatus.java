package com.byaffe.microtasks.models;

public enum TaskStatus {
    DRAFT(0,"Draft"),
    SUBMITTED(1,"Submitted, pending approval"),
    PAID_ACTIVE(2,"Paid Active"),
    COMPLETED(3,"Completed"),
    CANCELLED(-1,"Cancelled"),;
    private String uiName;
    private int id;

    TaskStatus(int id, String name) {
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

    public static TaskStatus getById(int id){
        for(TaskStatus enumValue: TaskStatus.values()){
            if(enumValue.id==id){
                return enumValue;
            }
        }
        return null;
    }
}
