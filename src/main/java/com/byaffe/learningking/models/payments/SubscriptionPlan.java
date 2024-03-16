package com.byaffe.learningking.models.payments;

import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseAcademyType;
import com.byaffe.learningking.models.courses.CourseCategory;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.shared.models.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "subscription_plans")
public class SubscriptionPlan extends BaseEntity {

    private String name;
    private String description;
    private String imageUrl;
    @Enumerated(EnumType.STRING)
    private SubscriptionContentRestrictionType contentRestrictionType;
    private int maximumNumberOfCourses = 1;
    private int durationInMonths = 1;
    private float cost;
    @Enumerated(EnumType.STRING)
    private PublicationStatus publicationStatus = PublicationStatus.INACTIVE;
    @Enumerated(EnumType.STRING)
    private CourseAcademyType allowedAcademyType;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinTable(name = "plan_courses", joinColumns = @JoinColumn(name = "plan_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> courses;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinTable(name = "plan_course_categories", joinColumns = @JoinColumn(name = "plan_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<CourseCategory> courseCategories;

    public void addCourse(Course course) {
        if (this.courses == null) {
            this.courses = new HashSet<>();
        }
        this.courses.add(course);
    }

    public void addCourseCategory(CourseCategory category) {
        if (this.courseCategories == null) {
            this.courseCategories = new HashSet<>();
        }
        this.courseCategories.add(category);
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
