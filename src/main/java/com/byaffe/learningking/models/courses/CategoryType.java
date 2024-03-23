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
    COURSE("Course",0),
    CERTIFICATION("Certification",1),
    ARTICLE("Article",2);

    private String displayName;
private int id;
    CategoryType(String uiName,int id) {
        this.displayName = uiName;
        this.id=id;
    }

    public static CategoryType getById(int id){
        for(CategoryType enumValue: CategoryType.values()){
            if(enumValue.id==id){
                return enumValue;
            }
        }
        return null;
    }

    public String getDisplayName() {
        return displayName;
    }

}
