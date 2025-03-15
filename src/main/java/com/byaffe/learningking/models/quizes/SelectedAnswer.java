package com.byaffe.learningking.models.quizes;

import com.byaffe.learningking.shared.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "selected_answers")
public class SelectedAnswer extends BaseEntity {

    @JsonIncludeProperties({"id", "score"})
    @ManyToOne
    @JoinColumn(name = "quiz_attempt_id")
    private QuizAttempt quizAttempt;
    @JsonIncludeProperties({"id", "name", "position"})
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    @Column(name = "score")
    private Double score;
    @Column(name = "correctly_answered")
    private Boolean correctlyAnswered=false;
    @Column(name = "submitted_response", length = 1000)
    private String submittedResponse;
    @Column(name = "correct_response", length = 1000)
    private String correctResponse;
    @Column(name = "attachment_response_url", length = 1000)
    private String attachmentResponseUrl;


}
