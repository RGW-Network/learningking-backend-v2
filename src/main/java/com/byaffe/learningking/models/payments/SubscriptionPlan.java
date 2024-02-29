/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.byaffe.learningking.models.payments;

import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseAcademyType;
import com.byaffe.learningking.models.courses.CourseCategory;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.shared.models.BaseEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Ray Gdhrt
 */
@Entity
@Table(name = "subscription_plans")
public class SubscriptionPlan extends BaseEntity {

    private String name;
    private String description;
    private String imageUrl;
    private SubscriptionContentRestrictionType contentRestrictionType;
    private int maximumNumberOfCourses = 1;
    private int durationInMonths = 1;
    private float cost;
    private PublicationStatus publicationStatus = PublicationStatus.INACTIVE;
    private CourseAcademyType allowedAcademyType;
    private Set<Course> courses;
    private Set<CourseCategory> courseCategories;

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description", columnDefinition = "TEXT")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "maximum_number_of_courses")
    public int getMaximumNumberOfCourses() {
        return maximumNumberOfCourses;
    }

    public void setMaximumNumberOfCourses(int maximumNumberOfCourses) {
        this.maximumNumberOfCourses = maximumNumberOfCourses;
    }

    @Column(name = "duration_in_months")
    public int getDurationInMonths() {
        return durationInMonths;
    }

    public void setDurationInMonths(int durationInMonths) {
        this.durationInMonths = durationInMonths;
    }

    @Column(name = "image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Enumerated()
    @Column(name = "allowed_academy_type")
    public CourseAcademyType getAllowedAcademyType() {
        return allowedAcademyType;
    }

    public void setAllowedAcademyType(CourseAcademyType allowedAcademyType) {
        this.allowedAcademyType = allowedAcademyType;
    }

    @Column(name = "cost")
    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "publication_status", nullable = true)
    public PublicationStatus getPublicationStatus() {
        return publicationStatus;
    }

    public void setPublicationStatus(PublicationStatus publicationStatus) {
        this.publicationStatus = publicationStatus;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "content_restriction_type")
    public SubscriptionContentRestrictionType getContentRestrictionType() {
        return contentRestrictionType;
    }

    public void setContentRestrictionType(SubscriptionContentRestrictionType contentRestrictionType) {
        this.contentRestrictionType = contentRestrictionType;
    }

    /**
     * @return the bibleVerses
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinTable(name = "plan_courses", joinColumns = @JoinColumn(name = "plan_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    public Set<Course> getCourses() {
        return courses;
    }

    public void addCourse(Course course) {
        if (this.courses == null) {
            this.courses = new HashSet<>();
        }

        this.courses.add(course);
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinTable(name = "plan_course_categories", joinColumns = @JoinColumn(name = "plan_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    public Set<CourseCategory> getCourseCategories() {
        return courseCategories;
    }

    public void addCourseCategory(CourseCategory category) {
        if (this.courseCategories == null) {
            this.courseCategories = new HashSet<>();
        }

        this.courseCategories.add(category);
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public void setCourseCategories(Set<CourseCategory> courseCategories) {
        this.courseCategories = courseCategories;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof SubscriptionPlan && (super.getId() != null)
                ? super.getId().equals(((SubscriptionPlan) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }

}
