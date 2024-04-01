package com.byaffe.learningking.models;

public enum TaskVerificationType {
    MANUAL(0,"Manual"),
    AUTOMATED(1,"Automatic");
    private String uiName;
    private int id;

    TaskVerificationType(int id, String name) {
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

    public static TaskVerificationType getById(int id){
        for(TaskVerificationType enumValue: TaskVerificationType.values()){
            if(enumValue.id==id){
                return enumValue;
            }
        }
        return null;
    }
}
