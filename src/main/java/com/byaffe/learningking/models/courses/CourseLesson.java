package com.byaffe.learningking.models.courses;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.byaffe.learningking.shared.models.BaseEntity;
import lombok.Data;

@Data
@Entity
@Table(name = "course_lessons")
public class CourseLesson extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "position", length = 10)
    private int position = 1;

    @Column(name = "cover_image_url", columnDefinition = "TEXT")
    private String coverImageUrl;

    @Column(name = "video_url", columnDefinition = "TEXT")
    private String videoUrl;

    @Column(name = "audio_url", columnDefinition = "TEXT")
    private String audioUrl;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "publication_status", nullable = true)
    private PublicationStatus publicationStatus;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "full_description", columnDefinition = "TEXT")
    private String fullDescription;



    @Override
    public boolean equals(Object object) {
        return object instanceof CourseLesson && (super.getId() != null) ? super.getId().equals(((CourseLesson) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }

}
