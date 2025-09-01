package com.byaffe.learningking.models.courses;

import javax.persistence.*;

import com.byaffe.learningking.shared.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import com.byaffe.learningking.models.quizes.Quiz;

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

    @Column(name = "is_preview")
    private Boolean isPreview = false;


    @Column(name = "cover_image_url", columnDefinition = "TEXT")
    private String coverImageUrl;

    @Column(name = "video_url", columnDefinition = "TEXT")
    private String videoUrl;

    @Column(name = "content_type")
    @Enumerated(EnumType.STRING)
private ContentType contentType;

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

    // Bidirectional mapping to LectureQuiz
    @JsonIgnore
    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LectureQuiz> lectureQuizzes = new ArrayList<>();



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

    // Helper methods for managing lecture quizzes
    public void addLectureQuiz(LectureQuiz lectureQuiz) {
        if (this.lectureQuizzes == null) {
            this.lectureQuizzes = new ArrayList<>();
        }
        lectureQuiz.setLecture(this);
        this.lectureQuizzes.add(lectureQuiz);
    }

    public void removeLectureQuiz(LectureQuiz lectureQuiz) {
        if (this.lectureQuizzes != null) {
            this.lectureQuizzes.remove(lectureQuiz);
            lectureQuiz.setLecture(null);
        }
    }

    /**
     * Get all quizzes associated with this lecture
     */
    public List<Quiz> getQuizzes() {
        List<Quiz> quizzes = new ArrayList<>();
        if (this.lectureQuizzes != null) {
            for (LectureQuiz lectureQuiz : this.lectureQuizzes) {
                if (lectureQuiz.getQuiz() != null) {
                    quizzes.add(lectureQuiz.getQuiz());
                }
            }
        }
        return quizzes;
    }

    /**
     * Get active quizzes for this lecture
     */
    public List<Quiz> getActiveQuizzes() {
        List<Quiz> activeQuizzes = new ArrayList<>();
        if (this.lectureQuizzes != null) {
            for (LectureQuiz lectureQuiz : this.lectureQuizzes) {
                if (lectureQuiz.getQuiz() != null && 
                    lectureQuiz.getPublicationStatus() == PublicationStatus.ACTIVE &&
                    lectureQuiz.getQuiz().getPublicationStatus() == PublicationStatus.ACTIVE) {
                    activeQuizzes.add(lectureQuiz.getQuiz());
                }
            }
        }
        return activeQuizzes;
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