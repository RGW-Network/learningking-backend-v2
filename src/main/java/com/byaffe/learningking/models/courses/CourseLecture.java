package com.byaffe.learningking.models.courses;

import javax.persistence.*;

import com.byaffe.learningking.shared.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "course_lectures")
public class CourseLecture extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "body", columnDefinition = "TEXT")
    private String body;

    @Column(name = "position", length = 10)
    private int position = 1;


    @Column(name = "cover_image_url", columnDefinition = "TEXT")
    private String coverImageUrl;

    @Column(name = "video_url", columnDefinition = "TEXT")
    private String videoUrl;

    @Column(name = "audio_url", columnDefinition = "TEXT")
    private String audioUrl;

    @Column(name = "full_description", columnDefinition = "TEXT")
    private String fullDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "publication_status", nullable = true)
    private PublicationStatus publicationStatus= PublicationStatus.ACTIVE;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "series_part_verses", joinColumns = @JoinColumn(name = "series_part_id"), inverseJoinColumns = @JoinColumn(name = "verse_id"))
    private Set<ExternalResource> externalLinks;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "course_topic_id")
    private CourseTopic courseTopic;



    public void addExternalLink(ExternalResource link) {
        if (this.externalLinks == null) {
            this.externalLinks = new HashSet<>();
        }
        this.externalLinks.add(link);
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
        return object instanceof CourseLecture && (super.getId() != null) ? super.getId().equals(((CourseLecture) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}