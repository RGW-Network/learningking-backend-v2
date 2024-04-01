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
public enum PublicationStatus {
    ACTIVE(0,"Active"),
    INACTIVE(1,"UnPublished");
    private int id;
    private String displayName;

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    PublicationStatus(int id, String name){
        this.id=id;
        this.displayName=name;
    }
    public static PublicationStatus getById(int id){
        for(PublicationStatus enumValue: PublicationStatus.values()){
            if(enumValue.id==id){
                return enumValue;
            }
        }
        return null;
    }
}
