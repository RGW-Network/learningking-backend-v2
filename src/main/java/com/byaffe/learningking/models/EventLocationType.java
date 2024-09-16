package com.byaffe.learningking.models;

public enum EventLocationType {
    ONLINE(0,"Online"),
    PHYSICAL(1,"Physical"),

    ;
    private String uiName;
    private int id;

    EventLocationType(int id, String name) {
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

    public static EventLocationType getById(int id){
        for(EventLocationType enumValue: EventLocationType.values()){
            if(enumValue.id==id){
                return enumValue;
            }
        }
        return null;
    }
}
