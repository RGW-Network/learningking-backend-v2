package com.byaffe.learningking.shared.models;

public enum MessageTemplateParams {
    USERNAME("{Username}"),
    FIRST_NAME("{FirstName}"),
    LAST_NAME("{LastName}"),
    PHONE_NUMBER("{PhoneNumber}"),
    EMAIL_ADDRESS("{EmailAddress}"),
    OTP("{OTP}"),
    VERIFICATION_LINK("{VerificationLink}");
    private String displayName;

    MessageTemplateParams( String name) {
        this.displayName = name;
    }

    public String getDisplayName() {
        return displayName;
    }

}
