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
@Table(name = "course_sub_topics")
public class CourseSubTopic extends BaseEntity {

    private static final long serialVersionUID = 1L;
    private String title;
    private String body;
    private int position = 1;
    private boolean isLastStage = false;
    private boolean isFirstStage = false;
    private String imageUrl;
    private PublicationStatus publicationStatus;
    private CourseTopic courseTopic;
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

    @Column(name = "body", columnDefinition = "TEXT")
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Column(name = "position", length = 10)
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Column(name = "is_last_stage", length = 10)
    public boolean isIsLastStage() {
        return isLastStage;
    }

    public void setIsLastStage(boolean isLastStage) {
        this.isLastStage = isLastStage;
    }

     @Column(name = "is_first_stage", length = 10)
    public boolean isIsFirstStage() {
        return isFirstStage;
    }

    public void setIsFirstStage(boolean isFirstStage) {
        this.isFirstStage = isFirstStage;
    }

      @Column(name = "image_url", columnDefinition = "TEXT")
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @ManyToOne
    @JoinColumn(name = "course_lesson_id")
    public CourseTopic getCourseTopic() {
        return courseTopic;
    }

    public void setCourseTopic(CourseTopic courseTopic) {
        this.courseTopic = courseTopic;
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
        return object instanceof CourseSubTopic && (super.getId() != null) ? super.getId().equals(((CourseSubTopic) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }

}
