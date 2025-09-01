package com.byaffe.learningking.models.courses;

import com.byaffe.learningking.models.quizes.Quiz;
import com.byaffe.learningking.shared.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "lecture_quizes")
public class LectureQuiz extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Column(name = "position", length = 10)
    private int position = 1;

    @Enumerated(EnumType.STRING)
    @Column(name = "publication_status", nullable = true)
    private PublicationStatus publicationStatus= PublicationStatus.ACTIVE;

    @JsonIncludeProperties({"title","id"})
    @ManyToOne
    @JoinColumn(name = "lecture_id")
    private CourseLecture lecture;

    @JsonIncludeProperties({"name","id"})
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    // Helper methods for managing bidirectional relationships
    public void setLecture(CourseLecture lecture) {
        this.lecture = lecture;
        if (lecture != null && !lecture.getLectureQuizzes().contains(this)) {
            lecture.addLectureQuiz(this);
        }
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
        if (quiz != null && !quiz.getLectureQuizzes().contains(this)) {
            quiz.addLectureQuiz(this);
        }
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof LectureQuiz && (super.getId() != null) ? super.getId().equals(((LectureQuiz) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}