package com.byaffe.learningking.controllers.constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.core.Response;
import org.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;

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

    public static Response composeSuccessMessage(String successMessage) throws JSONException {
        JSONObject result = new JSONObject();
        result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
        result.put(ApiUtils.RESPONSE_PARAM, successMessage);

        return buidResponse(200, result);
    }

    public static Response composeFailureMessage(String errorMessage) throws JSONException {
        JSONObject result = new JSONObject();
        result.put(ApiUtils.STATUS_PARAM, ApiUtils.FAILURE_TOKEN);
        result.put(ApiUtils.RESPONSE_PARAM, errorMessage);
        return buidResponse(403, result);
    }

    public static Response composeFailureMessage(String errorMessage, int statusCode) throws JSONException {
        JSONObject result = new JSONObject();
        result.put(ApiUtils.STATUS_PARAM, statusCode);
        result.put(ApiUtils.RESPONSE_PARAM, errorMessage);

        return buidResponse(statusCode, result);
    }

    public static Response buidResponse(int status, JSONObject result) {
        return Response
                .status(status)
                .entity("" + result)
                .build();
    }

    public static Response buidSuccessResponse(JSONObject result) throws JSONException {
        result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
        result.put(ApiUtils.RESPONSE_PARAM, ApiUtils.SUCCESSFUL_RESPONSE_VALUE);
        return Response
                .status(200)
                .entity("" + result)
                .build();

    }

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
