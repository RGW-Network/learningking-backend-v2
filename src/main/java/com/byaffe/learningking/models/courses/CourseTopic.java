package com.byaffe.learningking.models.courses;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.byaffe.learningking.shared.models.BaseEntity;

@Entity
@Table(name = "course_topics")
public class CourseTopic extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @Column(name = "title", length = 100)
    private String title;
    @Column(name = "full_description",columnDefinition = "TEXT" )
    private String description;
    private int position = 1;
    private boolean isLast = false;
    private boolean isFirst = false;

    private PublicationStatus publicationStatus;
    private CourseLesson courseLesson;


    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }
     
     

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

     @Column(name = "is_last", length = 10)
    public boolean isIsLast() {
        return isLast;
    }

    public void setIsLast(boolean isLast) {
        this.isLast = isLast;
    }

     @Column(name = "is_first", length = 10)
    public boolean isIsFirst() {
        return isFirst;
    }

    public void setIsFirst(boolean isFirst) {
        this.isFirst = isFirst;
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

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "series_part_verses", joinColumns = @JoinColumn(name = "series_part_id"), inverseJoinColumns = @JoinColumn(name = "verse_id"))
    public Set<ExternalResource> getExternalLinks() {
        return externalLinks;
    }

    public void setExternalLinks(Set<ExternalResource> externalLinks) {
        this.externalLinks = externalLinks;
    }
    
     
   public void addExternalLink(ExternalResource link) {
        if (this.externalLinks == null) {
            this.externalLinks = new HashSet<>();
        }

        this.externalLinks.add(link);
    }

    @ManyToOne
    @JoinColumn(name = "course_lesson_id")
    public CourseLesson getCourseLesson() {
        return courseLesson;
    }

    public void setCourseLesson(CourseLesson courseLesson) {
        this.courseLesson = courseLesson;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "publication_status", nullable = true)
    public PublicationStatus getPublicationStatus() {
        return publicationStatus;
    }

    public void setPublicationStatus(PublicationStatus publicationStatus) {
        this.publicationStatus = publicationStatus;
    }

    public void addLink(ExternalResource link) {
        if (this.externalLinks == null) {
            this.externalLinks = new HashSet<ExternalResource>();
        }

        this.externalLinks.add(link);
    }

    public void removeLink(ExternalResource link) {
        this.externalLinks.remove(link);
    }

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
