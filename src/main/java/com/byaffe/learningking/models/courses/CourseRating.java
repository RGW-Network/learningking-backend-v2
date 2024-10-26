package com.byaffe.learningking.models.courses;

import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.shared.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "course_ratings_new")
public class CourseRating extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @Column(name = "review_text", length = 1000)
    private String reviewText;


    @Column(name = "stars_count")
    private Double starsCount;//out of 5
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "student_reference")
    private Student student;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "course_reference")
    private Course course;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name="status")
    private PublicationStatus publicationStatus = PublicationStatus.ACTIVE;

    @JoinColumn(name="featured")
    private Boolean featured;

    @Override
    public boolean equals(Object object) {
        return object instanceof CourseRating && (super.getId() != null) ? super.getId().equals(((CourseRating) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }

}
