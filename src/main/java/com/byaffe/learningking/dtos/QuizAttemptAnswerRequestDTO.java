package com.byaffe.learningking.dtos;

import lombok.Data;

@Data
public class QuizAttemptAnswerRequestDTO {
    private Long questionId;
    private Long selectedAnswerId;

}
