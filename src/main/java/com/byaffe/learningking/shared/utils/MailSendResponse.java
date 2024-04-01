package com.byaffe.learningking.shared.utils;

public class MailSendResponse {

    public MailSendResponse(String errorMessage, boolean isSuccessful) {
        this.errorMessage = errorMessage;
        this.isSuccessful = isSuccessful;
    }

    public MailSendResponse(boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    private String errorMessage;
    private boolean isSuccessful;
}
