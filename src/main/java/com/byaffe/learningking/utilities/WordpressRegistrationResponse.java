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
public class WordpressRegistrationResponse extends WordpressBaseResponse {

    public int id;
    public String username;
    public String name;
    public String first_name;
    public String last_name;
    public String email;
    public String url;
    public String description;
    public String link;
    public String locale;
    public String nickname;
    public String slug;
    
     public WordpressRegistrationResponse(int code, String message) {
        super(code, message);
    }

}
