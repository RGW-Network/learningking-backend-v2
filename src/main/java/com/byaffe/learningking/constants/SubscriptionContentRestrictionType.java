/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.byaffe.learningking.constants;

/**
 *
 * @author Ray Gdhrt
 */
public enum SubscriptionContentRestrictionType {
    OPEN_ANY_COURSE("Any Course"),
    SELECTED_COURSES("Selected Courses"),
     SELECTED_CATEGORY("Selected Categories"),
    SELECTED_ACADEMY("Selected Academy");
    private String displayName;
    
    
    SubscriptionContentRestrictionType(String uiName){
        this.displayName=uiName;
        
    }

    public String getDisplayName() {
        return displayName;
    }

    
}
