package com.byaffe.learningking.models.courses;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.byaffe.learningking.shared.models.BaseEntity;

@Entity
@Table(name = "course_lessons")
public class CourseLesson extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String title;
    private String description;
    private int position = 1;
    private String coverImageUrl;
    private String videoUrl;
    private String audioUrl;
    private PublicationStatus publicationStatus;
    private Course course;

     private String fullDescription;

      @Column(name = "full_description",columnDefinition = "TEXT" )
    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }
     
     
    @Column(name = "title", length = 100)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

     @Column(name = "description", columnDefinition = "TEXT")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "position", length = 10)
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


      @Column(name = "cover_image_url", columnDefinition = "TEXT")
    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

     @Column(name = "video_url", columnDefinition = "TEXT")
    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Column(name = "audio_url", columnDefinition = "TEXT")
    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
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
        return object instanceof CourseLesson && (super.getId() != null) ? super.getId().equals(((CourseLesson) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }

}
