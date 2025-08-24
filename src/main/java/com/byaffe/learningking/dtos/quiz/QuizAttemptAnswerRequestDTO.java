package com.byaffe.learningking.dtos.quiz;

import lombok.Data;

@Data
public class QuizAttemptAnswerRequestDTO {
    public Long id;
    private String title;
    private String description;
    private Long lectureId;

}
