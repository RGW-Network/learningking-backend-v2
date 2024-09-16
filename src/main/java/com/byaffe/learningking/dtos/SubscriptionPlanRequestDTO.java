package com.byaffe.learningking.dtos;

import com.byaffe.learningking.models.courses.Category;
import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseAcademyType;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.models.payments.SubscriptionContentRestrictionType;

import javax.persistence.*;
import java.util.Set;

public class SubscriptionPlanRequestDTO {

    private String name;
    private String description;
    private String imageUrl;
    private SubscriptionContentRestrictionType contentRestrictionType;
    private int maximumNumberOfCourses = 1;
    private int maximumNumberOfStudents = 1;
    private int durationInMonths = 1;
    private float cost;
    private PublicationStatus publicationStatus = PublicationStatus.INACTIVE;
    private CourseAcademyType allowedAcademyType;
    private Set<Long> courseIds;
    private Set<Long> courseCategoryIds;
}
