package com.byaffe.learningking.dtos.quiz;

import com.byaffe.learningking.models.quizes.SelectedAnswer;
import lombok.Data;

import java.util.List;

@Data
public class QuizAttemptRequestDTO {
    private Long id;
    private Long enrollmentId;
    private Long quizId;
    private Integer score;
    private List<SelectedAnswer> selectedAnswers;

}
