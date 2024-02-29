/*
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.byaffe.learningking.models.courses;

/**
 *
 * @author Ray Gdhrt
 */
public enum CourseAcademyType {
    PROFFESSIONAL("Proffessional"),
    WEALTHY_MINDS("Wealthy Minds");
    
    
    private String displayName;
    CourseAcademyType(String uiName){
    this.displayName=uiName;
    }

    public String getDisplayName() {
        return displayName;
    }

    
    
}
