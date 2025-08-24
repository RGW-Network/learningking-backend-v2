package com.byaffe.learningking.models.quizes;

import com.byaffe.learningking.shared.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "quiz_questions")
public class Question extends BaseEntity {

    private String name;
private Integer position;
    private Double marks;
    @Enumerated(EnumType.STRING)
    private QuestionResponseType responseType;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<AnswerOption> answerOptions;

    @Transient
    public String getQuizName(){
        return quiz.getTitle();
    }

    @Transient
    public Long getQuizId(){
        return quiz.getId();
    }
    // Getters and Setters
}
