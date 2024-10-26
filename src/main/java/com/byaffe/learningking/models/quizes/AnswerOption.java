package com.byaffe.learningking.models.quizes;

import com.byaffe.learningking.shared.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "question_answer_options")
public class AnswerOption extends BaseEntity {
    private String name;
    private Boolean correct;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @Transient
    public String getQuestionName(){
        return question.getName();
    }

    @Transient
    public Long getQuestionId(){
        return question.getId();
    }
}
