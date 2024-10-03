package com.byaffe.learningking.models.quizes;

import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.courses.CourseEnrollment;
import com.byaffe.learningking.shared.models.BaseEntity;
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

    @ManyToOne
    @JoinColumn(name = "enrollment_id")
    private CourseEnrollment enrollment;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    private LocalDateTime attemptDate;

    private Integer score;

    @OneToMany(mappedBy = "quizAttempt", cascade = CascadeType.ALL)
    private List<SelectedAnswer> selectedAnswers;

    // Getters and Setters
}
