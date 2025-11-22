package com.byaffe.learningking.controllers;

import com.byaffe.learningking.dtos.InitQuizRequestDTO;
import com.byaffe.learningking.dtos.quiz.*;
import com.byaffe.learningking.models.Article;
import com.byaffe.learningking.models.courses.LectureQuiz;
import com.byaffe.learningking.models.quizes.Question;
import com.byaffe.learningking.models.quizes.Quiz;
import com.byaffe.learningking.models.quizes.QuizAttempt;
import com.byaffe.learningking.services.ArticleService;
import com.byaffe.learningking.services.LectureQuizService;
import com.byaffe.learningking.services.QuizAttemptService;
import com.byaffe.learningking.services.QuizService;
import com.byaffe.learningking.services.impl.QuizServiceImpl;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.security.SessionContext;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.googlecode.genericdao.search.Search;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Ray Gdhrt
 */
@Slf4j
@RestController
@RequestMapping("api/v1/quizes")
public class QuizController {
    @Autowired
    QuizService quizService;

    @Autowired
    QuizAttemptService quizAttemptService;

@Autowired
    LectureQuizService lectureQuizService;

@Autowired
ModelMapper modelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<LectureQuizResponseDTO>> getById(@PathVariable(name = "id") long id) throws JSONException {
        LectureQuiz quiz = lectureQuizService.getInstanceByIDOrThrow(id);
        LectureQuizResponseDTO responseDTO=modelMapper.map(quiz,LectureQuizResponseDTO.class);
        responseDTO.setAttempts(quizAttemptService.getQuizAttempts(new Search().addFilterEqual("lectureQuiz",quiz).addFilterEqual("enrollment.student", SessionContext.getLoggedInStudent()),0,0));
        responseDTO.setQuestions(quiz.getQuiz().getQuestions());
        return ResponseEntity.ok().body(new ResponseObject<>(responseDTO));

    }

    @GetMapping("/attempt/{id}")
    public ResponseEntity<ResponseObject<QuizAttempt>> getById(@PathVariable(name = "id") Long id) throws JSONException {
        QuizAttempt quiz = quizAttemptService.getById(id);
        return ResponseEntity.ok().body(new ResponseObject<>(quiz));

    }
    @PostMapping("/start")
    public ResponseEntity<ResponseObject<QuizAttempt>> start(@RequestBody InitQuizRequestDTO dto) throws JSONException {
        QuizAttempt quiz = quizAttemptService.init(dto.getQuizId(),dto.getCourseEnrollmentId());
        return ResponseEntity.ok().body(new ResponseObject<>(quiz));
    }
    @PostMapping("/submit")
    public ResponseEntity<ResponseObject<QuizAttempt>> submitQuizAttempt(@RequestBody QuizAttemptSSubmissionRequestDTO dto) throws JSONException {
        QuizAttempt quiz = quizAttemptService.saveQuizAttempt(dto);
        return ResponseEntity.ok().body(new ResponseObject<>(quiz));
    }

    @GetMapping("/questions")
    public ResponseEntity<ResponseList<Question>> getQuestions(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                               @RequestParam(value = "offset", required = true) Integer offset,
                                                               @RequestParam(value = "limit", required = true) Integer limit,
                                                               @RequestParam(value = "sortBy", required = false) String sortBy,
                                                               @RequestParam(value = "sortDescending", required = false) Boolean sortDescending,
                                                               @RequestParam(value = "quizId", required = false) Long quizId) throws JSONException {
        Search search = QuizServiceImpl.generateSearchTermsForQuizes(searchTerm)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        if (quizId != null) {
            search.addFilterEqual("quiz.id", quizId);
        }
        if (StringUtils.isNotEmpty(sortBy)) {
            search.addSort(sortBy, sortDescending);
        }
        List<Question> models = quizService.getQuizQuestions(search, offset, limit);
        long count = quizService.countQuizQuestions(search);
        return ResponseEntity.ok().body(new ResponseList<>(models, (int) count, offset, limit));


    }



    @GetMapping("")
    public ResponseEntity<ResponseList<Quiz>> getQuizes(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                        @RequestParam(value = "offset", required = true) Integer offset,
                                                        @RequestParam(value = "limit", required = true) Integer limit,
                                                        @RequestParam(value = "sortBy", required = false) String sortBy,
                                                        @RequestParam(value = "sortDescending", required = false) Boolean sortDescending,
                                                        @RequestParam(value = "lectureId", required = false) Long lectureId,
                                                        @RequestParam(value = "courseId", required = false) Long courseId) throws JSONException {

        Search search = QuizServiceImpl.generateSearchTermsForQuizes(searchTerm)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        if (lectureId != null) {
            search.addFilterEqual("courseLecture.id", lectureId);
        }
        if (courseId != null) {
            search.addFilterEqual("courseLecture.courseTopic.courseLesson.course.id", courseId);
        }

        if (StringUtils.isNotEmpty(sortBy)) {
            search.addSort(sortBy, sortDescending);
        }
        List<Quiz> models = quizService.getQuizes(search, offset, limit);
        long count = quizService.countQuizes(search);
        return ResponseEntity.ok().body(new ResponseList<>(models, (int) count, offset, limit));

    }


}
