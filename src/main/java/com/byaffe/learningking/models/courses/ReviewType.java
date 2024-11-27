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
public enum ReviewType {
    GENERAL("General"),
    COURSE("Course"),
    EVENT("Event"),
    CERTIFICATION("Certification"),
    ARTICLE("Article")
    ;
    private String displayName;


    public String getDisplayName() {
        return displayName;
    }

    ReviewType( String name){
        this.displayName=name;
    }



}
