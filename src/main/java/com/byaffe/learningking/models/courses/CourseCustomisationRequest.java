package com.byaffe.learningking.models.courses;

import com.byaffe.learningking.constants.PreferredModeOfDelivery;
import com.byaffe.learningking.models.SubmissionStatus;
import com.byaffe.learningking.shared.models.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@ToString(callSuper = true)
@Entity
@Table(name = "course_customisation_requests")
public class CourseCustomisationRequest extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @Column(length = 1000)
    private String courseName;
    private Long targetTrainees;
    @JoinColumn(name = "category_id")
    private Category courseCategory;//category
    @Enumerated(EnumType.STRING)
    private PreferredModeOfDelivery preferredModeOfDelivery;//enum
    @Column(columnDefinition = "TEXT")
    private String courseDetails;
    private String expectedOutComes;
    @Enumerated(EnumType.STRING)
    private SubmissionStatus status;

    @Override
    public boolean equals(Object object) {
        return object instanceof CourseCustomisationRequest && (super.getId() != null) ? super.getId().equals(((CourseCustomisationRequest) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
