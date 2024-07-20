package com.byaffe.learningking.config;

/**
 * Holds  key names of the environment variables used throughout the MS
 */
public interface MessageLabels {
    String USER_REGISTRATION_EMAIL_SUBJECT = "Learningking Confirmation";
    String USER_REGISTRATION_EMAIL_CONTENT = "<p>Your Learningking account has been created successfully. One more step.</p>\n" +
            "\n" +

            "<p>Your one time code is; <span style=\"color:#2980b9\"><h1>{0}</strong></h1></p>\n" +
            "\n" +
            "<p>Use this to activate your account</p>";

    String INSTRUCTOR_REGISTRATION_EMAIL_SUBJECT = "Learning King Instructors";
    String INSTRUCTOR_REGISTRATION_EMAIL_CONTENT = "<p>Your Learningking Instructor account has been created successfully. One more step.</p>\n" +
            "\n" +

            "<p>Login with your email as the username and your registered phone-number as the password; <span style=\"color:#2980b9\"><h1>{0}</strong></h1></p>\n" +
            "\n" +
            "<p>Regards, LK Admin</p>";

}