package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.daos.AnswerDao;
import com.byaffe.learningking.daos.QuestionDao;
import com.byaffe.learningking.daos.QuizAttemptDao;
import com.byaffe.learningking.daos.QuizDao;
import com.byaffe.learningking.dtos.quiz.QuizAttemptAnswerRequestDTO;
import com.byaffe.learningking.dtos.quiz.QuizAttemptRequestDTO;
import com.byaffe.learningking.models.quizes.QuizAttempt;
import com.byaffe.learningking.models.quizes.SelectedAnswer;
import com.byaffe.learningking.services.CourseEnrollmentService;
import com.byaffe.learningking.services.QuizAttemptService;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.utils.CustomSearchUtils;
import com.googlecode.genericdao.search.Search;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    CourseEnrollmentService courseEnrollmentService;

    @Autowired
    ModelMapper modelMapper;

    public static Search generateSearchTermsForQuizes(String searchTerm) {

        return CustomSearchUtils.generateSearchTerms(searchTerm, Arrays.asList("title", "description"));
    }

    @Override
    public QuizAttempt saveQuizAttempt(QuizAttemptRequestDTO dto) throws ValidationFailedException {
        if (dto.getQuizId()==null) {
            throw new ValidationFailedException("Missing quiz id");
        }
        if (dto.getEnrollmentId()==null) {
            throw new ValidationFailedException("Missing enrollment id");
        }
        QuizAttempt quiz = modelMapper.map(dto, QuizAttempt.class);
        
        double totalScore = 0;
        //save the answers 
        for (QuizAttemptAnswerRequestDTO answer : dto.getAnswers()) {
            SelectedAnswer selectedAnswer = new SelectedAnswer();
            selectedAnswer.setQuizAttempt(quiz);
            selectedAnswer.setQuestion(questionDao.findById(answer.getQuestionId()).orElseThrow(()->new ValidationFailedException("Question not found")));
            selectedAnswer.setSubmittedResponse(answerDao.findById(answer.getSelectedAnswerId()).orElseThrow(()->new ValidationFailedException("Selected answer not found")).getName());
            
            if(selectedAnswer.getQuestion().getAnswerOptions().stream().anyMatch(answerOption -> answerOption.getId() == answer.getSelectedAnswerId())){
                selectedAnswer.setCorrectlyAnswered(true);
            }else{
                selectedAnswer.setCorrectlyAnswered(false);
            }
            selectedAnswer.setScore(selectedAnswer.getQuestion().getMarks());
            totalScore += selectedAnswer.getScore();
        }
        quiz.setEnrollment(courseEnrollmentService.getInstanceByIDOrNull(dto.getEnrollmentId()));
        quiz.setQuiz(quizDao.getReference(dto.getQuizId()));

        quiz.setScore((int) totalScore);

        return quizAttemptDao.save(quiz);
    }

    @Override
    public QuizAttempt init(long quizId, long enrollmentId) throws ValidationFailedException {
        QuizAttempt quizAttempt= new QuizAttempt();
        quizAttempt.setQuiz(quizDao.findById(quizId).orElseThrow(()->new ValidationFailedException("Quize not found")));
        quizAttempt.setEnrollment(courseEnrollmentService.getInstanceByIDOrNull(enrollmentId));
        return quizAttemptDao.save(quizAttempt);
    }

    @Override
    public List<QuizAttempt> getQuizAttempts(Search search, int offset, int limit) {
        search.setMaxResults(limit).setFirstResult(offset);
        return quizDao.search(search);
    }

    @Override
    public long countQuizAttempts(Search search) {
        return quizDao.count(search);
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
