package com.byaffe.learningking.models.payments;

import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseAcademyType;
import com.byaffe.learningking.models.courses.Category;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.shared.models.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.*;

@Data
@ToString(callSuper = true)
@Entity
@Table(name = "subscription_plans")
public class SubscriptionPlan extends BaseEntity {

    private String name;
    private String color;
    @Column(name = "description", length = 2000)
    private String description;
    @Column(name = "what_you_get", length = 2000)
    private String commaSeparatedWhatYouGet;
    private String imageUrl;
    private int maximumNumberOfWealthyMindsCourses = 1;
    private int maximumNumberOfCorporateCourses = 1;
    private int maximumNumberOfStudents = 1;
    private int maximumNumberOfWealthyMindsCertifications = 1;
    private int maximumNumberOfCorporateCertifications = 1;
    private int durationInMonths = 1;
    private double costPerYear;
    private double costPerMonth;
    @Enumerated(EnumType.STRING)
    private PublicationStatus publicationStatus = PublicationStatus.INACTIVE;

    public List<String> getWhatYouGet(){
        if (StringUtils.isNotEmpty(this.commaSeparatedWhatYouGet)) {
            return Arrays.asList(this.commaSeparatedWhatYouGet.split(","));
        } else {
            return new ArrayList<>(); // Return an empty list if the string is empty or null
        }

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
