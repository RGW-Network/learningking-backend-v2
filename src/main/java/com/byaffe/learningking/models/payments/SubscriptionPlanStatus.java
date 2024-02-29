/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.byaffe.learningking.models.payments;

/**
 *
 * @author Ray Gdhrt
 */
public enum SubscriptionPlanStatus {
    ACTIVE("Active"),
    DEPLETED("Depleted"),
     EXPIRED("Expired");
    private String displayName;
    
    
    SubscriptionPlanStatus(String uiName){
        this.displayName=uiName;
        
    }

    public String getDisplayName() {
        return displayName;
    }

    
}
