package com.byaffe.learningking.models.quizes;

import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.courses.CourseEnrollment;
import com.byaffe.learningking.models.courses.CourseLecture;
import com.byaffe.learningking.shared.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "quiz_attempts")
public class QuizAttempt extends BaseEntity {

    @JsonIncludeProperties({"id","progress"})
    @ManyToOne
    @JoinColumn(name = "enrollment_id")
    private CourseEnrollment enrollment;

    @JsonIncludeProperties({"id", "title"})
    @ManyToOne
    @JoinColumn(name = "lecture_id")
    private CourseLecture lecture;

    @JsonIncludeProperties({"id", "title"})
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    private Integer score;
    private LocalDateTime startedOnDate;
    private LocalDateTime completedOnDate;


    @OneToMany(mappedBy = "quizAttempt", cascade = CascadeType.ALL)
    private List<SelectedAnswer> selectedAnswers;

    // Getters and Setters
}
