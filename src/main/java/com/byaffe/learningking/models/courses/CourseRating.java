package com.byaffe.learningking.models.courses;

import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.shared.models.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "course_ratings")
public class CourseRating extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @Column(name = "review_text", length = 1000)
    private String reviewText;
    @Column(name = "stars_count")
    private Double starsCount;//out of 5
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Enumerated(EnumType.STRING)
    private PublicationStatus publicationStatus = PublicationStatus.ACTIVE;


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
