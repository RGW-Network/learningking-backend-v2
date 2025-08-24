package com.byaffe.learningking.models;

public enum EventAttendanceStatus {
    ATTENDING("Attending"),
    CANCELLED("Cancelled"),
    COMPLETED("Completed");
    private String displayName;

    EventAttendanceStatus(String name) {
        this.displayName = name;
    }

    public String getDisplayName() {
        return displayName;
    }


}
