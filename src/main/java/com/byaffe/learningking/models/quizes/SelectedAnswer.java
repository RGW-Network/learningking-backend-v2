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
    @Column(name = "score")
    private Double score;
    @Column(name = "text_response", length = 1000)
    private String textResponse;
    @Column(name = "attachment_response_url", length = 1000)
    private String attachmentResponseUrl;


}
