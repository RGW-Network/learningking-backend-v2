package com.byaffe.learningking.models.courses;

import com.byaffe.learningking.shared.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "course_topics")
public class CourseTopic extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @Column(name = "title", length = 100)
    private String title;
    @Column(name = "description",columnDefinition = "TEXT" )
    private String description;
    @Column(name = "position", length = 10)
    private int position = 1;

    @Enumerated(EnumType.STRING)
    @Column(name = "publication_status", nullable = true)
    private PublicationStatus publicationStatus= PublicationStatus.ACTIVE;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "course_lesson_id")
    private CourseLesson courseLesson;


    @Override
    public boolean equals(Object object) {
        return object instanceof CourseTopic && (super.getId() != null) ? super.getId().equals(((CourseTopic) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }

}
