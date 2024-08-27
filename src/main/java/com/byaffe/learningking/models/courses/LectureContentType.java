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
public enum LectureContentType {
    VIDEO("Video",0),
    TEXT("Text",1);
    private String displayName;
private int id;
    LectureContentType(String uiName, int id) {
        this.displayName = uiName;
        this.id=id;
    }

    public static LectureContentType getById(int id){
        for(LectureContentType enumValue: LectureContentType.values()){
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
