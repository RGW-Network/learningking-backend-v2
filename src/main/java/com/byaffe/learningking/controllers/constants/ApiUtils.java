package com.byaffe.learningking.controllers.constants;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiUtils {

    public static final String SUCCESSFUL_TOKEN = "200";
    public static final String FAILURE_TOKEN = "400";
    public static final String STATUS_PARAM = "status";
    public static final String RESPONSE_PARAM = "message";
    public static final String SUCCESSFUL_RESPONSE_VALUE = "";
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("E, dd MMM yyyy");
    public static final SimpleDateFormat SHORT_DATE_FORMAT = new SimpleDateFormat("E, dd MMM");
    public static final SimpleDateFormat SHORT_TIME_FORMAT = new SimpleDateFormat("hh:mm a");
    public static final SimpleDateFormat ENGLISH_DATE_FORMAT = new SimpleDateFormat("E, dd MMM yyyy");
    public static final SimpleDateFormat ENGLISH_TIME_ONLY_FORMAT = new SimpleDateFormat("HH:mm a");



    public static String getYouTubeId(String youTubeUrl) {
        String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(youTubeUrl);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }

}
