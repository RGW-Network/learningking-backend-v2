package com.byaffe.learningking.config;

/**
 * Holds  key names of the environment variables used throughout the MS
 */
public interface MessageLabels {
    String USER_REGISTRATION_EMAIL_SUBJECT = "Kacuba Email Confirmation";
    String USER_REGISTRATION_EMAIL_CONTENT = "<p>Your Kacuba account has been created successfully. One more step.</p>\n" +
            "\n" +

            "<p>Your one time code is; <span style=\"color:#2980b9\"><h1>{0}</strong></h1></p>\n" +
            "\n" +
            "<p>Use this to activate your account</p>";

}