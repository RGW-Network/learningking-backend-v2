package com.byaffe.learningking.models;

public enum TaskLocality {
    INTERNATIONAL(0,"International"),
    LOCAL(1,"Local");
    private String uiName;
    private int id;

    TaskLocality(int id, String name) {
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

    public static TaskLocality getById(int id){
        for(TaskLocality enumValue: TaskLocality.values()){
            if(enumValue.id==id){
                return enumValue;
            }
        }
        return null;
    }
}
