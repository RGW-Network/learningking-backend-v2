/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.byaffe.learningking.utilities;

/**
 *
 * @author Ray Gdhrt
 */
public class WordpressResponse {
public int statusCode;
    public String token;
    public String user_email;
    public String user_nicename;
    public String user_display_name;
    public String api_version;
    public String code;
    public String message;

    public WordpressResponse(int code, String message) {
        this.statusCode = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "WordpressResponse{" + "token=" + token + ", user_email=" + user_email + ", user_nicename=" + user_nicename + ", user_display_name=" + user_display_name + ", api_version=" + api_version + '}';
    }
    
    public boolean isSuccessfull(){
        return this.statusCode>=200&&this.statusCode<=299;
    }

}
