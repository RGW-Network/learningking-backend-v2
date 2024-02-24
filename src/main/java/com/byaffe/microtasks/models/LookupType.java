package com.byaffe.microtasks.models;

public enum LookupType {
    TASK_CATEGORIES(1,"Task Categories"),

    ;
    private String uiName;
    private int id;

    LookupType(int id,String name) {
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

    public static LookupType getById(int id){
        for(LookupType enumValue: LookupType.values()){
            if(enumValue.id==id){
                return enumValue;
            }
        }
        return null;
    }
}
