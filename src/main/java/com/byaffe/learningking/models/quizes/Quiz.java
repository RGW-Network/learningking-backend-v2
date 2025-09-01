package com.byaffe.learningking.models.quizes;

import com.byaffe.learningking.models.Article;
import com.byaffe.learningking.models.courses.CourseLecture;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.shared.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import com.byaffe.learningking.models.courses.LectureQuiz;
import com.byaffe.learningking.models.courses.CourseLecture;

@Data
@NoArgsConstructor
@Entity
@Table(name = "quizes")
public class Quiz extends BaseEntity {

    private String title;
    private Long durationInMinutes=5L;
    private String description;


    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<Question> questions;

    @Enumerated(EnumType.STRING)
    @Column(name = "publication_status", nullable = true)
    private PublicationStatus publicationStatus= PublicationStatus.ACTIVE;

    // Bidirectional mapping to LectureQuiz
    @JsonIgnore
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LectureQuiz> lectureQuizzes = new ArrayList<>();

    // Helper methods for managing lecture quizzes
    public void addLectureQuiz(LectureQuiz lectureQuiz) {
        if (this.lectureQuizzes == null) {
            this.lectureQuizzes = new ArrayList<>();
        }
        lectureQuiz.setQuiz(this);
        this.lectureQuizzes.add(lectureQuiz);
    }

    public void removeLectureQuiz(LectureQuiz lectureQuiz) {
        if (this.lectureQuizzes != null) {
            this.lectureQuizzes.remove(lectureQuiz);
            lectureQuiz.setQuiz(null);
        }
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Quiz && (super.getId() != null) ? super.getId().equals(((Quiz) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }

}
