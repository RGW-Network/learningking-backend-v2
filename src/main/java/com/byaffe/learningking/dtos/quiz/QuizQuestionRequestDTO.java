package com.byaffe.learningking.dtos.quiz;

import com.byaffe.learningking.models.quizes.AnswerOption;
import com.byaffe.learningking.models.quizes.QuestionResponseType;
import com.byaffe.learningking.models.quizes.Quiz;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
public class QuizQuestionRequestDTO {
    public Long id;
    public String name;
    public Integer position=1;
    private QuestionResponseType responseType=QuestionResponseType.MultipleChoice;
    private Long quizId;
    private List<AnswerOption> answerOptions;


}
