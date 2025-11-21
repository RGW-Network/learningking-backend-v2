package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.dtos.quiz.LectureQuizRequestDTO;
import com.byaffe.learningking.models.courses.CourseLecture;
import com.byaffe.learningking.models.courses.LectureQuiz;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.models.quizes.Quiz;
import com.byaffe.learningking.services.*;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.utilities.ImageStorageService;
import com.googlecode.genericdao.search.Search;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class LectureQuizeServiceImpl extends GenericServiceImpl<LectureQuiz> implements LectureQuizService {

    @Autowired
    ImageStorageService imageStorageService;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    QuizService quizService;

    @Autowired
    CourseLectureService courseLectureService;


    @Override
    public LectureQuiz save(LectureQuizRequestDTO dto) throws ValidationFailedException {

        if (dto.getLectureId() == null) {
            throw new ValidationFailedException("Missing lecture");
        }
        if (dto.getQuizId() == null) {
            throw new ValidationFailedException("Missing quiz");
        }
        CourseLecture lecture = courseLectureService.getInstanceByIDOrThrow(dto.getLectureId());
        Quiz quiz = quizService.getById(dto.getQuizId());

        LectureQuiz exists = searchUnique(new Search().addFilterEqual("quiz.id", dto.getQuizId()).addFilterEqual("lecture.id", dto.getLectureId()).addFilterEqual("recordStatus", RecordStatus.ACTIVE));

        if (exists != null && !exists.getId().equals(dto.getId())) {
            throw new ValidationFailedException("This quiz already exists on this lecture exists!");
        }
        LectureQuiz lectureQuiz = new LectureQuiz();
        if (dto.getId() != null && dto.getId() > 0) {
            lectureQuiz = getInstanceByIDOrThrow(dto.id);
        }
        Double passMark = dto.getPassMark() == null ? 50D : dto.getPassMark();
        if (passMark < 0 || passMark > 100) {
            throw new ValidationFailedException("Pass mark must be between 0 and 100");
        }
        lectureQuiz.setPublicationStatus(dto.getPublicationStatus());
        lectureQuiz.setLecture(lecture);
        lectureQuiz.setQuiz(quiz);
        lectureQuiz.setPosition(dto.getPosition());
        lectureQuiz.setPassMark(passMark);
        return save(lectureQuiz);

    }




    @Override
    public List<String> getStringFilterFields() {
        return Collections.emptyList();
    }

    /**
     * Get all quizzes for a specific lecture
     */
    public List<Quiz> getQuizzesForLecture(Long lectureId) {
        CourseLecture lecture = courseLectureService.getInstanceByIDOrThrow(lectureId);
        return lecture.getActiveQuizzes();
    }


    /**
     * Get all lecture quizzes for a specific lecture
     */
    public List<LectureQuiz> getLectureQuizzesForLecture(Long lectureId) {
        Search search = new Search();
        search.addFilterEqual("lecture.id", lectureId);
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        search.addSort("position", true);
        return search(search);
    }

    /**
     * Get all lecture quizzes for a specific quiz
     */
    public List<LectureQuiz> getLectureQuizzesForQuiz(Long quizId) {
        Search search = new Search();
        search.addFilterEqual("quiz.id", quizId);
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        search.addSort("position", true);
        return search(search);
    }

    /**
     * Check if a quiz is associated with a lecture
     */
    public boolean isQuizAssociatedWithLecture(Long quizId, Long lectureId) {
        Search search = new Search();
        search.addFilterEqual("quiz.id", quizId);
        search.addFilterEqual("lecture.id", lectureId);
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        return searchUnique(search) != null;
    }

    /**
     * Get lecture quiz by lecture and quiz IDs
     */
    public LectureQuiz getLectureQuizByLectureAndQuiz(Long lectureId, Long quizId) {
        Search search = new Search();
        search.addFilterEqual("lecture.id", lectureId);
        search.addFilterEqual("quiz.id", quizId);
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        return searchUnique(search);
    }


}
