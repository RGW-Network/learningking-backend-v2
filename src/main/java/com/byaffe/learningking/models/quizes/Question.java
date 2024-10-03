package com.byaffe.learningking.models.quizes;

import com.byaffe.learningking.shared.models.BaseEntity;
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
    @Enumerated(EnumType.STRING)
    private QuestionResponseType responseType;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<AnswerOption> answerOptions;


    // Getters and Setters
}
