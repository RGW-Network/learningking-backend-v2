package com.byaffe.learningking.dtos;

import lombok.Data;

@Data
public class InitQuizRequestDTO {
    private Long quizId;
    private Long courseEnrollmentId;

}
