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
    PROFFESSIONAL("Proffessional",0),
    WEALTHY_MINDS("Wealthy Minds",1);
    
    
    private String displayName;
    private int id;
    CourseAcademyType(String uiName,int id){
    this.displayName=uiName;
    this.id=id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static CourseAcademyType getById(int id){
        for(CourseAcademyType enumValue: CourseAcademyType.values()){
            if(enumValue.id==id){
                return enumValue;
            }
        }
        return null;
    }
    
}
