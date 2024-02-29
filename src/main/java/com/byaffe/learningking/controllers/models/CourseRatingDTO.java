package com.byaffe.learningking.controllers.models;

import java.util.Set;
import org.byaffe.systems.api.ApiSecurity;

public class CourseRatingDTO extends ApiSecurity {

    private String courseId;
    private String ratingText;
    private float stars;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getRatingText() {
        return ratingText;
    }

    public void setRatingText(String ratingText) {
        this.ratingText = ratingText;
    }

    public float getStars() {
        return stars;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }

  
}
