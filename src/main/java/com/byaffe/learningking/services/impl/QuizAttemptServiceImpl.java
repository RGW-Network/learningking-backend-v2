package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.daos.AnswerDao;
import com.byaffe.learningking.daos.QuestionDao;
import com.byaffe.learningking.daos.QuizAttemptDao;
import com.byaffe.learningking.daos.QuizDao;
import com.byaffe.learningking.dtos.quiz.QuizAttemptAnswerRequestDTO;
import com.byaffe.learningking.dtos.quiz.QuizAttemptRequestDTO;
import com.byaffe.learningking.dtos.quiz.QuizAttemptSSubmissionRequestDTO;
import com.byaffe.learningking.models.quizes.QuizAttempt;
import com.byaffe.learningking.models.quizes.SelectedAnswer;
import com.byaffe.learningking.services.CourseEnrollmentService;
import com.byaffe.learningking.services.LectureQuizService;
import com.byaffe.learningking.services.QuizAttemptService;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.utils.CustomSearchUtils;
import com.googlecode.genericdao.search.Search;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class QuizAttemptServiceImpl implements QuizAttemptService {

    @Autowired
    QuizDao quizDao;
    @Autowired
    QuizAttemptDao quizAttemptDao;
    @Autowired
    QuestionDao questionDao;
    @Autowired
    AnswerDao answerDao;

    @Autowired
    LectureQuizService lectureQuizService;

    @Autowired
    CourseEnrollmentService courseEnrollmentService;

    @Autowired
    ModelMapper modelMapper;

    public static Search generateSearchTermsForQuizes(String searchTerm) {

        return CustomSearchUtils.generateSearchTerms(searchTerm, Arrays.asList("title", "description"));
    }

    @Override
    public QuizAttempt saveQuizAttempt(QuizAttemptSSubmissionRequestDTO dto) throws ValidationFailedException {
        if (dto.getQuizAttemptId()==null) {
            throw new ValidationFailedException("Missing quiz id");
        }

        QuizAttempt quiz= getById(dto.getQuizAttemptId());

        double totalScore = 0;
        quiz.setSelectedAnswers(new ArrayList<>());
        //save the answers 
        for (QuizAttemptAnswerRequestDTO answer : dto.getAnswers()) {
            SelectedAnswer selectedAnswer = new SelectedAnswer();
            selectedAnswer.setQuizAttempt(quiz);
            selectedAnswer.setQuestion(questionDao.findById(answer.getQuestionId()).orElseThrow(()->new ValidationFailedException("Question not found")));
            selectedAnswer.setSubmittedResponse(answerDao.findById(answer.getSelectedAnswerId()).orElseThrow(()->new ValidationFailedException("Selected answer not found")).getName());
            selectedAnswer.setCorrectlyAnswered(selectedAnswer.getQuestion().getAnswerOptions().stream().anyMatch(answerOption -> answerOption.getId() == answer.getSelectedAnswerId()));
            selectedAnswer.setScore(selectedAnswer.getQuestion().getMarks());
            if(selectedAnswer.getCorrectlyAnswered()) {
                totalScore += selectedAnswer.getScore();
            }

            quiz.getSelectedAnswers().add(selectedAnswer);
        }
        double passMark=dto.getAnswers().size();
        if(quiz.getLectureQuiz().getPassMark()!=null){
            passMark=quiz.getLectureQuiz().getPassMark();
        }

        quiz.setStatus(passMark<totalScore? QuizAttempt.QuizAttemptStatus.Passed: QuizAttempt.QuizAttemptStatus.Failed);
       quiz.setCompletedOnDate(LocalDateTime.now());
        quiz.setScore((int) totalScore);

        return quizAttemptDao.merge(quiz);
    }

    @Override
    public QuizAttempt init(long quizId, long enrollmentId) throws ValidationFailedException {
        QuizAttempt quizAttempt= new QuizAttempt();
        QuizAttempt lastAttempt = quizAttemptDao.searchUnique(new Search().addFilterEqual("enrollment.id",enrollmentId).addFilterEqual("lectureQuiz.id",quizId).addFilterEqual("status", QuizAttempt.QuizAttemptStatus.Ongoing));
        if(lastAttempt!=null){
            quizAttempt=lastAttempt;

        }else {
            quizAttempt.setLectureQuiz(lectureQuizService.getInstanceByIDOrThrow(quizId));
            quizAttempt.setEnrollment(courseEnrollmentService.getInstanceByIDOrNull(enrollmentId));
        }
        quizAttempt.setStartedOnDate(LocalDateTime.now());
        return quizAttemptDao.save(quizAttempt);
    }

    @Override
    public List<QuizAttempt> getQuizAttempts(Search search, int offset, int limit) {
        search.setMaxResults(limit).setFirstResult(offset);
        return quizAttemptDao.search(search);
    }

    @Override
    public long countQuizAttempts(Search search) {
        return quizAttemptDao.count(search);
    }

    @Override
    public QuizAttempt saveQuizAttemptAnswer(QuizAttemptAnswerRequestDTO dto) throws ValidationFailedException {
        return null;
    }

    @Override
    public QuizAttempt getById(Long id) throws ValidationFailedException {
        return quizAttemptDao.findById(id).orElseThrow(() -> new ValidationFailedException("Record Not Found"));
    }


    public void delete(Long id) {
       // return quizAttemptDao.findById(id).orElseThrow(() -> new ValidationFailedException("Record Not Found"));
    }



}
