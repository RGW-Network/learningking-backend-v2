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
public enum CourseOwnerShipType {
     OPEN(0,"Open"),
    COMPANY_ONLY(1,"Company Only");
private int id;
private String displayName;
    CourseOwnerShipType(int id, String name){
        this.id=id;
        this.displayName=name;
    }
    
}
