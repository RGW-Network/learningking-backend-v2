package com.byaffe.learningking.models;

public enum LookupType {
    EVENT_CATEGORIES("Event Categories"),
    WORK_STATUSES("Work Statuses"),
    NATURE_OF_BUSINESS("Nature Of Business"),
    PROFESSIONS("Professions") ;
    private String uiName;

    LookupType(String name) {
        this.uiName = name;
    }

    public String getUiName() {
        return uiName;
    }

    public void setUiName(String uiName) {
        this.uiName = uiName;
    }


}
