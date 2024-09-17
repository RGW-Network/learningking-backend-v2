package com.byaffe.learningking.models.payments;

import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.shared.models.BaseEntity;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.*;

@Data
@ToString(callSuper = true)
@Entity
@Table(name = "new_subscription_plans")
public class SubscriptionPlan extends BaseEntity {

    private String name;
    private String color="#FFD700";
    @Column(name = "description", length = 2000)
    private String description;
    @Column(name = "what_you_get", length = 2000)
    private String commaSeparatedWhatYouGet;
    private String imageUrl;
    private Integer maximumNumberOfWealthyMindsCourses = 1;
    private Integer maximumNumberOfCorporateCourses = 1;
    private Integer maximumNumberOfStudents = 1;
    private Integer maximumNumberOfWealthyMindsCertifications = 1;
    private Integer maximumNumberOfCorporateCertifications = 1;
    private Integer durationInMonths = 1;
    private Double costPerYear;
    private Double costPerMonth;
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
