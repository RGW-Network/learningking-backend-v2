package com.byaffe.learningking.dtos.quiz;

import com.byaffe.learningking.models.quizes.QuestionResponseType;
import lombok.Data;

import java.util.List;

@Data
public class QuizQuestionRequestDTO {
    public Long id;
    public String name;
    public Integer position=1;
    private QuestionResponseType responseType=QuestionResponseType.MultipleChoice;
    private Long quizId;
    private Double marks;
    private List<AnswerRequestDTO> answerOptions;

    @Data
  public static class AnswerRequestDTO {
        public Long id;
        public String name;
        public Boolean correct=false;
    }
}
