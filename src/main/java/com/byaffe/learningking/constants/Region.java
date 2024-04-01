/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.byaffe.learningking.constants;

/**
 *
 * @author RayGdhrt
 */
public enum Region {

    CENTRAL("Central"),
    NORTHERN("Northern"),
    EASTERN("Eastern"),
    WESTERN("Western"),
    WEST_NILE("West nile"),
    DIASPORA("Diasporah"),
    OTHER("Others");

    String name;

    Region(String uiName) {
        this.name = uiName;

    }

    public static Region fromString(String text) {
        for (Region b : Region.values()) {
            if (b.name.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    

}
