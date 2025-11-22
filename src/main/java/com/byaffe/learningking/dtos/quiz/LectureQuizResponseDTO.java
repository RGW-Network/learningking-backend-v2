package com.byaffe.learningking.dtos.quiz;

import com.byaffe.learningking.models.courses.LectureQuiz;
import com.byaffe.learningking.models.quizes.Question;
import com.byaffe.learningking.models.quizes.QuizAttempt;
import lombok.Data;

import java.util.List;

@Data
public class LectureQuizResponseDTO extends LectureQuiz {
    private String attemptStatus="None";
    private List<QuizAttempt> attempts;
    private List<Question> questions;


}
