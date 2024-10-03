package com.byaffe.learningking.models.quizes;

import com.byaffe.learningking.shared.models.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "selected_answers")
public class SelectedAnswer extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "quiz_attempt_id")
    private QuizAttempt quizAttempt;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "answer_id")
    private AnswerOption answerOption;

    // Getters and Setters
}
