package com.byaffe.learningking.models.quizes;

import com.byaffe.learningking.shared.models.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table(name = "question_answer_options")
public class AnswerOption extends BaseEntity {
    private String name;
    private Boolean correct;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    // Getters and Setters
}
