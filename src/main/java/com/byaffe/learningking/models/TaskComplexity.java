package com.byaffe.learningking.models;

public enum TaskComplexity {
    STARTER(0,"Starter"),
    INTERMEDIATE(1,"Intermediate"),
    ADVANCED(2,"Advanced");
    private String uiName;
    private int id;

    TaskComplexity(int id, String name) {
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

    public static TaskComplexity getById(int id){
        for(TaskComplexity enumValue: TaskComplexity.values()){
            if(enumValue.id==id){
                return enumValue;
            }
        }
        return null;
    }
}
