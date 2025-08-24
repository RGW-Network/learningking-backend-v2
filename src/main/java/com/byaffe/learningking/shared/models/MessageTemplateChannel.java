package com.byaffe.learningking.shared.models;

public enum MessageTemplateChannel {
    SMS("Sms"),
    EMAIL("Email"),
    PUSH_NOTIFICATION("Push Notification");
    private String displayName;

    MessageTemplateChannel(String name) {
        this.displayName = name;
    }

    public String getDisplayName() {
        return displayName;
    }
}
