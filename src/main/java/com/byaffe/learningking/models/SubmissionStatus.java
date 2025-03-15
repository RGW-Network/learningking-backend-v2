/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.byaffe.learningking.models;

/**
 * @author Ray Gdhrt
 */
public enum SubmissionStatus {
    Pending("Pending"),
    Resolved("Resolved");

    private final String uiName;

    SubmissionStatus(String uiName) {
        this.uiName = uiName;
    }

    public String getUiName() {
        return uiName;
    }
}
