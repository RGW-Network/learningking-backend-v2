package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.daos.AnswerDao;
import com.byaffe.learningking.daos.QuestionDao;
import com.byaffe.learningking.daos.QuizDao;
import com.byaffe.learningking.dtos.quiz.AnswerRequestDTO;
import com.byaffe.learningking.dtos.quiz.QuizQuestionRequestDTO;
import com.byaffe.learningking.dtos.quiz.QuizRequestDTO;
import com.byaffe.learningking.models.quizes.AnswerOption;
import com.byaffe.learningking.models.quizes.Question;
import com.byaffe.learningking.models.quizes.Quiz;
import com.byaffe.learningking.services.*;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.utils.CustomSearchUtils;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class QuizServiceImpl implements QuizService {

    @Autowired
    QuizDao quizDao;
    @Autowired
    QuestionDao questionDao;
    @Autowired
    AnswerDao answerDao;

    @Autowired
    CourseLectureService lectureService;

    @Autowired
    ModelMapper modelMapper;

    public static Search generateSearchTermsForCompanyStudent(String searchTerm) {

        return CustomSearchUtils.generateSearchTerms(searchTerm, Arrays.asList("title", "description"));
    }

    @Override
    public Quiz saveQuiz(QuizRequestDTO dto) throws ValidationFailedException {
        if (StringUtils.isEmpty(dto.getTitle())) {
            throw new ValidationFailedException("Missing title");
        }
        if (StringUtils.isEmpty(dto.getDescription())) {
            throw new ValidationFailedException("Missing description");
        }
        Quiz quiz = modelMapper.map(dto, Quiz.class);
        quiz.setCourseLecture(lectureService.getInstanceByID(dto.getCourseLectureId()));

        return quizDao.save(quiz);
    }

    @Override
    public List<Quiz> getQuizes(Search search, int offset, int limit) {
        search.setMaxResults(limit).setFirstResult(offset);
        return quizDao.search(search);
    }

    @Override
    public long countQuizes(Search search) {
        return quizDao.count(search);
    }

    @Override
    public Quiz getById(Long id) throws ValidationFailedException {
        return quizDao.findById(id).orElseThrow(() -> new ValidationFailedException("Record Not Found"));
    }

    @Override
    public Question saveQuizQuestion(QuizQuestionRequestDTO dto) throws ValidationFailedException {
        if (StringUtils.isEmpty(dto.getName())) {
            throw new ValidationFailedException("Missing title");
        }
        if (dto.getQuizId()==null) {
            throw new ValidationFailedException("Missing quiz id");
        }
        Question quiz = modelMapper.map(dto, Question.class);
        quiz.setQuiz(getById(dto.getQuizId()));
        //todo some position reorganisation

        return questionDao.save(quiz);
    }


    @Override
    public List<Question> getQuizQuestions(Search search, int offset, int limit) {
        search.setMaxResults(limit).setFirstResult(offset);
        return questionDao.search(search);
    }

    @Override
    public long countQuizQuestions(Search search) {
        return questionDao.count(search);
    }

    public Question getQuestionById(Long id) throws ValidationFailedException {
        return questionDao.findById(id).orElseThrow(() -> new ValidationFailedException("Record Not Found"));
    }
    @Override
    public AnswerOption saveAnswerOption(AnswerRequestDTO dto) throws ValidationFailedException {
        if (StringUtils.isEmpty(dto.getName())) {
            throw new ValidationFailedException("Missing name");
        }
        if (dto.getQuestionId()==null) {
            throw new ValidationFailedException("Missing question id");
        }
        AnswerOption quiz = modelMapper.map(dto, AnswerOption.class);
        quiz.setQuestion(getQuestionById(dto.getQuestionId()));

        return answerDao.save(quiz);
    }


}
