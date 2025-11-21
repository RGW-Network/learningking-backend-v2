package com.byaffe.learningking.dtos.quiz;

import lombok.Data;

@Data
public class QuizAttemptAnswerRequestDTO {
    public Long id;
    private Long questionId;
    private Long selectedAnswerId;

}
