/*
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.byaffe.learningking.models.courses;

import com.byaffe.learningking.models.OwnershipType;

/**
 *
 * @author Ray Gdhrt
 */
public enum CourseOwnerShipType {
     OPEN(0,"Open"),
    COMPANY_ONLY(1,"Company Only");
private int id;
private String displayName;

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    CourseOwnerShipType(int id, String name){
        this.id=id;
        this.displayName=name;
    }
    public static CourseOwnerShipType getById(int id){
        for(CourseOwnerShipType enumValue: CourseOwnerShipType.values()){
            if(enumValue.id==id){
                return enumValue;
            }
        }
        return null;
    }
    @Override
    public String toString() {
        return displayName;
    }
}
