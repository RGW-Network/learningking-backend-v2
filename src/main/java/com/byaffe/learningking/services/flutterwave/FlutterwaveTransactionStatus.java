/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.byaffe.learningking.services.flutterwave;

/**
 *
 * @author RAYGDHRT
 */
public enum FlutterwaveTransactionStatus {
    FAILED("failed"),
    CANCELLED("cancelled"),
    ERROR("error"),
    ABORTED("aborted"),
    SUCCESSFULL("successful");

    private final String statusName;

    @Override
    public String toString() {
        return this.statusName;
    }

    private FlutterwaveTransactionStatus(String name) {
        this.statusName = name;
    }

    public String getStatusName() {
        return statusName;
    }
    
    
    
    
    
}
