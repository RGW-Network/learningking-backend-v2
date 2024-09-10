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
public enum CategoryType {
    COURSE("Courses"),
    CERTIFICATION("Certifications"),
    ARTICLE("Articles"),
    EVENT("Events");

    private String displayName;
    CategoryType(String uiName) {
        this.displayName = uiName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
