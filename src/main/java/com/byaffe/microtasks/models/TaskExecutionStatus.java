package com.byaffe.microtasks.models;

public enum TaskExecutionStatus {
    DRAFT(0,"Draft"),
    SUBMITTED(1,"Submitted"),
    APPROVED(2,"Approved"),
    REJECTED(-1,"Rejected");
    private String uiName;
    private int id;

    TaskExecutionStatus(int id, String name) {
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

    public static TaskExecutionStatus getById(int id){
        for(TaskExecutionStatus enumValue: TaskExecutionStatus.values()){
            if(enumValue.id==id){
                return enumValue;
            }
        }
        return null;
    }
}
