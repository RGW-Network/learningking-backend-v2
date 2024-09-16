/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.byaffe.learningking.shared.constants;

/**
 *
 * @author RayGdhrt
 */
public enum RecordStatus {

    ACTIVE("Active"),
    DELETED("Delete"),
    ACTIVE_LOCKED("Active Loacked");

    private String displayName;

    RecordStatus(String name){
        this.displayName=name;
    }

    public String getDisplayName() {
        return displayName;
    }
}
