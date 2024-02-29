package com.byaffe.learningking.controllers;

/**
 *
 * @author RayGdhrt
 */
public interface ApiConstants {

    int SUCCESS_CODE = 200;//Ok
    int BAD_REQUEST_CODE = 403;//Some required parameter is missing or invalid
     int MALFORMED_REQUEST_CODE = 400;//Something in the request caused a server side exception
    int UNAUTHORISED_REQUEST_CODE = 401;//Unauthenticated
}
