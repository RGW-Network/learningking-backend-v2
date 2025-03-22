package com.byaffe.learningking.dtos.quiz;

import com.byaffe.learningking.models.quizes.SelectedAnswer;
import lombok.Data;

import java.util.List;

@Data
public class QuizAttemptRequestDTO {
    private Long enrollmentId;
    private Long quizId;
    private List<QuizAttemptAnswerRequestDTO> answers;

}
