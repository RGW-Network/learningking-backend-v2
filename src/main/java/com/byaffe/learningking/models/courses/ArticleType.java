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
public enum ArticleType {
    WEALTHY_MINDS_MAGAZINE("Wealthy Minds Magazine"),
    PROFESSIONAL_JOURNAL("Professional Journal"),
    CORPORATE_JOURNAL("Corporate Journal"),
    INVESTMENT_OPPORTUNITIES("Investment Opportunity"),
    RESEARCH("Research"),
    ADVERT("Adverts");

    private String displayName;

    ArticleType(String uiName) {
        this.displayName = uiName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
