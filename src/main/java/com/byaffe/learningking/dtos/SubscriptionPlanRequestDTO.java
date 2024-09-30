package com.byaffe.learningking.dtos;

import com.byaffe.learningking.models.courses.PublicationStatus;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SubscriptionPlanRequestDTO {
private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private int maximumNumberOfWealthyMindsCourses = 1;
    private int maximumNumberOfCorporateCourses = 1;
    private int maximumNumberOfStudents = 1;
    private int maximumNumberOfWealthyMindsCertifications = 1;
    private int maximumNumberOfCorporateCertifications = 1;
    private int durationInMonths = 1;
    private double costPerYear;
    private double costPerMonth;
    private PublicationStatus publicationStatus = PublicationStatus.INACTIVE;

    private List<String> whatYouGet= new ArrayList<>();
    public String  getCommaSeparatedWhatYouGet(){
        if(this.whatYouGet==null){
            return null;
        }
        return String.join(",", whatYouGet);

    }
}
