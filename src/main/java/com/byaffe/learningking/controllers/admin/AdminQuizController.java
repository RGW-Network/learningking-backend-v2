package com.byaffe.learningking.controllers.admin;

import com.byaffe.learningking.dtos.quiz.QuizQuestionRequestDTO;
import com.byaffe.learningking.dtos.quiz.QuizRequestDTO;
import com.byaffe.learningking.models.Article;
import com.byaffe.learningking.models.quizes.Question;
import com.byaffe.learningking.models.quizes.Quiz;
import com.byaffe.learningking.services.ArticleService;
import com.byaffe.learningking.services.QuizService;
import com.byaffe.learningking.services.impl.QuizServiceImpl;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.byaffe.learningking.shared.constants.PermissionConstant;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.security.SessionContext;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.googlecode.genericdao.search.Search;
import io.swagger.v3.oas.annotations.Hidden;
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
@Hidden
@RequestMapping("api/v1/admin/quizes")
public class AdminQuizController {
    @Autowired
    QuizService quizService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping("")
    public ResponseEntity<ResponseObject<Quiz>> addArticle(@RequestBody QuizRequestDTO dto) throws JSONException {
        SessionContext.permissionProtection(PermissionConstant.Course_Create);
        Quiz Article = quizService.saveQuiz(dto);
        return ResponseEntity.ok().body(new ResponseObject<>(Article));
    }
    @PostMapping("/question")
    public ResponseEntity<BaseResponse> addQuestion(@RequestBody QuizQuestionRequestDTO dto) throws JSONException {
        SessionContext.permissionProtection(PermissionConstant.Course_Create);
         quizService.saveQuizQuestion(dto);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }
    @PostMapping("/question/{id}/delete")
    public ResponseEntity<BaseResponse> deleteQuestion(@PathVariable long id) throws JSONException {
        SessionContext.permissionProtection(PermissionConstant.Course_Create);
        quizService.deleteQuestion(id);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }



    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<Quiz>> getById(@PathVariable(name = "id") long id) throws JSONException {
        SessionContext.permissionProtection(PermissionConstant.Course_Create);
        Quiz quiz = quizService.getById(id);
        return ResponseEntity.ok().body(new ResponseObject<>(quiz));

    }

    @GetMapping("/questions")
    public ResponseEntity<ResponseList<Question>> getQuestions(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                               @RequestParam(value = "offset", required = true) Integer offset,
                                                               @RequestParam(value = "limit", required = true) Integer limit,
                                                               @RequestParam(value = "sortBy", required = false) String sortBy,
                                                               @RequestParam(value = "sortDescending", required = false) Boolean sortDescending,
                                                               @RequestParam(value = "quizId", required = false) Long quizId) throws JSONException {
        SessionContext.permissionProtection(PermissionConstant.Course_Create);
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

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<BaseResponse> deleteArticle(@PathVariable long id) throws JSONException {
        SessionContext.permissionProtection(PermissionConstant.Course_Create);
        quizService.deletQuize(id);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }

    @GetMapping("")
    public ResponseEntity<ResponseList<Quiz>> getQuizes(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                        @RequestParam(value = "offset", required = true) Integer offset,
                                                        @RequestParam(value = "limit", required = true) Integer limit,
                                                        @RequestParam(value = "sortBy", required = false) String sortBy,
                                                        @RequestParam(value = "sortDescending", required = false) Boolean sortDescending,
                                                        @RequestParam(value = "lectureId", required = false) Long lectureId,
                                                        @RequestParam(value = "courseId", required = false) Long courseId) throws JSONException {

        SessionContext.permissionProtection(PermissionConstant.Course_Create);  Search search = QuizServiceImpl.generateSearchTermsForQuizes(searchTerm)
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

    @GetMapping("/v2/{id}")
    public ResponseEntity<ResponseObject<Article>> getArticleById(@PathVariable("id") Long id) throws JSONException {
        SessionContext.permissionProtection(PermissionConstant.Course_Create);  Article article = ApplicationContextProvider.getBean(ArticleService.class).getInstanceByID(id);

        return ResponseEntity.ok().body(new ResponseObject<>(article));
    }


}
