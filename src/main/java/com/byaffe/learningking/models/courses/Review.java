package com.byaffe.learningking.models.courses;

import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.shared.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "reviews")
public class Review extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "comment", length = 1000)
    private String comment;

    @Column(name = "stars_count")
    private Double starsCount;//out of 5

    @Column(name = "type")
    private ReviewType type;

    @Column(name = "record_id")
    private Long recordId;
    @Column(name = "record_name")
    private String recordName;//auto set by system

    @Enumerated(EnumType.STRING)
    @JoinColumn(name="status")
    private PublicationStatus publicationStatus = PublicationStatus.ACTIVE;

    @JoinColumn(name="featured")
    private Boolean featured;
    @Column(name = "reviewer_name")
    private String reviewerName;
    @Column(name = "reviewer_designation")
    private String reviewerDesignation;//title, job tittle,profession, location

    @Column(name = "reviewer_image_url")
    private String reviewerImageUrl;

    @Override
    public boolean equals(Object object) {
        return object instanceof Review && (super.getId() != null) ? super.getId().equals(((Review) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }

}
