package com.byaffe.learningking.services;

import com.byaffe.learningking.dtos.articles.ArticleRequestDTO;
import com.byaffe.learningking.dtos.quiz.LectureQuizRequestDTO;
import com.byaffe.learningking.models.Article;
import com.byaffe.learningking.models.courses.CourseLecture;
import com.byaffe.learningking.models.courses.LectureQuiz;
import com.byaffe.learningking.models.quizes.Quiz;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

import java.util.List;

/**
 * Responsible for CRUD operations on {@link Article}
 *
 * @author RayGdhrt
 *
 */
public interface LectureQuizService extends GenericService<LectureQuiz> {


    LectureQuiz save(LectureQuizRequestDTO dto) throws ValidationFailedException;

    // Utility methods for filtering
    List<Quiz> getQuizzesForLecture(Long lectureId);
    List<LectureQuiz> getLectureQuizzesForLecture(Long lectureId);
    List<LectureQuiz> getLectureQuizzesForQuiz(Long quizId);
    boolean isQuizAssociatedWithLecture(Long quizId, Long lectureId);
    LectureQuiz getLectureQuizByLectureAndQuiz(Long lectureId, Long quizId);






}
