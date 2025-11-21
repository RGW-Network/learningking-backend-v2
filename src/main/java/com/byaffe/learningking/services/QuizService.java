package com.byaffe.learningking.services;

import com.byaffe.learningking.dtos.quiz.BulkQuizQuestionUploadResponse;
import com.byaffe.learningking.dtos.quiz.QuizQuestionRequestDTO;
import com.byaffe.learningking.dtos.quiz.QuizRequestDTO;
import com.byaffe.learningking.models.Article;
import com.byaffe.learningking.models.quizes.Question;
import com.byaffe.learningking.models.quizes.Quiz;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.googlecode.genericdao.search.Search;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Responsible for CRUD operations on {@link Article}
 *
 * @author RayGdhrt
 *
 */
public interface QuizService  {


    Quiz saveQuiz(QuizRequestDTO dto) throws ValidationFailedException;
    List<Quiz> getQuizes(Search search, int offset, int limit) ;
    long countQuizes(Search search) ;
    Quiz getById(Long id) throws ValidationFailedException;

    Question saveQuizQuestion(QuizQuestionRequestDTO dto) throws ValidationFailedException;
    List<Question> getQuizQuestions(Search search, int offset, int limit) ;
    long countQuizQuestions(Search search) ;

    BulkQuizQuestionUploadResponse uploadQuizQuestionsFromCsv(Long quizId, MultipartFile file);


}
