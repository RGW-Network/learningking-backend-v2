package com.byaffe.learningking.dtos.quiz;

import lombok.Data;

import java.util.List;

@Data
public class QuizAttemptSSubmissionRequestDTO {
    private Long quizAttemptId;
    private List<QuizAttemptAnswerRequestDTO> answers;

}
