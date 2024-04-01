/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.byaffe.learningking.models;

import com.byaffe.learningking.models.courses.CourseAcademyType;

/**
 *
 * @author RayGdhrt
 */
public enum OwnershipType {
CHURCH("Church",0), RGW("RGW",1),SYSTEM("System",2);
    private int id;
    private String displayName;
    OwnershipType(String uiName,int id){
        this.displayName=uiName;
        this.id=id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static OwnershipType getById(int id){
        for(OwnershipType enumValue: OwnershipType.values()){
            if(enumValue.id==id){
                return enumValue;
            }
        }
        return null;
    }
}
