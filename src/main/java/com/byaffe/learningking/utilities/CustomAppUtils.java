/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.byaffe.learningking.utilities;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

/**
 * Formats a valid phone number to format 256xxxxxxx returns null if invalid
 * number is supplied
 */
public class CustomAppUtils {

    public static final int MAX_CONTENT_PACKAGES = 9;
    public static final int MAX_MESSAGES_PER_BATCH = 100;
    public static final int EGO_SMS_MESSAGE_COST = 30;
    public static final int MIN_TRANSACRION_AMOUNT = 1000;

    public static String validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        phoneNumber = phoneNumber.replace("+", "");
        if (phoneNumber.matches("\\d{12}") && phoneNumber.startsWith("256")) {
            return phoneNumber;
        } else if (phoneNumber.matches("\\d{9}") && phoneNumber.startsWith("7")) {
            return "256" + phoneNumber;
        } else if (phoneNumber.matches("\\d{10}") && phoneNumber.startsWith("07")) {
            return "256" + phoneNumber.substring(1);
        } else {
            return null;
        }
    }

    public static Date addMinutesToJavaUtilDate(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    public static Date addDayssToJavaUtilDate(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    public static String getURLWithContextPath(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath();
    }

}
