package com.byaffe.learningking.models;

import com.byaffe.learningking.models.courses.Category;
import com.byaffe.learningking.models.courses.CourseInstructor;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.shared.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_submissions")
public class UserSubmission extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "subject")
    private String subject;
    @Column(name = "fullName")
    private String fullName;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;


    @Column(name = "request_details", columnDefinition = "TEXT")
    private String requestDetails;
    @Column(name = "attachment_url")
    private String attachmentUrl;

    @Enumerated(EnumType.STRING)
    private SubmissionType type;


    @Enumerated(EnumType.STRING)
    private SubmissionStatus status=SubmissionStatus.Pending;

    @Transient
    public String getTypeName() {
        if (this.type == null) {
            return null;
        }
        return type.getUiName();
    }



    @Override
    public String toString() {
        return this.subject;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof UserSubmission && (super.getId() != null) ? super.getId().equals(((UserSubmission) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
