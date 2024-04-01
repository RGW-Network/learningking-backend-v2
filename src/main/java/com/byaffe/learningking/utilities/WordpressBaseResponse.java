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
public class WordpressBaseResponse {
public int statusCode;
    
    public String message;

    public WordpressBaseResponse(int code, String message) {
        this.statusCode = code;
        this.message = message;
    }

   
    public boolean isSuccessfull(){
        return this.statusCode>=200&&this.statusCode<=299;
    }

}
