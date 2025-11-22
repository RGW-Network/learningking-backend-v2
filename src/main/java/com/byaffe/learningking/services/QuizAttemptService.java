package com.byaffe.learningking.services;

import com.byaffe.learningking.dtos.quiz.*;
import com.byaffe.learningking.models.Article;
import com.byaffe.learningking.models.quizes.Question;
import com.byaffe.learningking.models.quizes.Quiz;
import com.byaffe.learningking.models.quizes.QuizAttempt;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.googlecode.genericdao.search.Search;

import java.util.List;

/**
 * Responsible for CRUD operations on {@link Article}
 *
 * @author RayGdhrt
 *
 */
public interface QuizAttemptService {


    QuizAttempt saveQuizAttempt(QuizAttemptSSubmissionRequestDTO dto) throws ValidationFailedException;
    QuizAttempt init(long quizId, long enrollmentId) throws ValidationFailedException;
    List<QuizAttempt> getQuizAttempts(Search search, int offset, int limit) ;
    long countQuizAttempts(Search search) ;
    QuizAttempt saveQuizAttemptAnswer(QuizAttemptAnswerRequestDTO dto) throws ValidationFailedException;

    QuizAttempt getById(Long id) throws ValidationFailedException;
    void delete(Long id) ;


}
