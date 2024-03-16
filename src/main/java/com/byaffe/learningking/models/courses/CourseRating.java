package com.byaffe.learningking.models.courses;

import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.shared.models.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "course_ratings")
public class CourseRating extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String reviewText;
    private float starsCount;//out of 5
    private Student student;
    private Course course;
    private PublicationStatus publicationStatus= PublicationStatus.ACTIVE;

    @Column(name = "review_text")
    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

     @Column(name = "stars_count")
    public float getStarsCount() {
        return starsCount;
    }

    public void setStarsCount(float starsCount) {
        this.starsCount = starsCount;
    }

    @ManyToOne
    @JoinColumn(name = "member_id")
    public Student getMember() {
        return student;
    }

    public void setMember(Student student) {
        this.student = student;
    }

     @ManyToOne
    @JoinColumn(name = "course_id")
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    
   
    @Enumerated(EnumType.STRING)
    @Column(name = "publication_status", nullable = true)
    public PublicationStatus getPublicationStatus() {
        return publicationStatus;
    }

    public void setPublicationStatus(PublicationStatus publicationStatus) {
        this.publicationStatus = publicationStatus;
    }


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
