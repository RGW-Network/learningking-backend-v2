package com.byaffe.learningking.models;

public enum EventStatus {
    UPCOMING(1,"Upcoming"),
    ONGOING(1,"Ongoing"),
    CANCELLED(1,"Cancelled"),
    COMPLETED(1,"Completed"),

    ;
    private String uiName;
    private int id;

    EventStatus(int id, String name) {
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

    public static EventStatus getById(int id){
        for(EventStatus enumValue: EventStatus.values()){
            if(enumValue.id==id){
                return enumValue;
            }
        }
        return null;
    }
}
