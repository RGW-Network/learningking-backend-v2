package com.byaffe.learningking.models;

/**
 *
 * @author RayGdhrt
 */
public enum NotificationDestinationActivity {
    HOME("home", "Home"),
    REGISTER("register", "Register"),
    LOGIN("login", "login"),
    RESET_PASSWORD("resetPassword", "Reset password"),
    DASHBOARD("dashboard", "Dashboard");
   

    String uiName;
    String appNavName;

    NotificationDestinationActivity(String navAppName, String name) {
        this.uiName = name;
        this.appNavName = navAppName;
    }

    public String getUiName() {
        return uiName;
    }

    public void setUiName(String uiName) {
        this.uiName = uiName;
    }

    public String getAppNavName() {
        return appNavName;
    }

    public void setAppNavName(String appNavName) {
        this.appNavName = appNavName;
    }

}
