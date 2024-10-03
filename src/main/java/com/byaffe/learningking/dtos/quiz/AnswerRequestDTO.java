package com.byaffe.learningking.dtos.quiz;

import com.byaffe.learningking.models.quizes.Question;
import lombok.Data;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
public class AnswerRequestDTO {
    public Long id;
    private String name;
    private Boolean correct;
    private Long questionId;


}
