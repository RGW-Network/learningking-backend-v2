package com.byaffe.learningking.controllers.admin;

import com.byaffe.learningking.daos.StudentDao;
import com.byaffe.learningking.dtos.DashboardDto;
import com.byaffe.learningking.dtos.articles.ArticleRequestDTO;
import com.byaffe.learningking.dtos.articles.ArticlesFilterDTO;
import com.byaffe.learningking.models.Article;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.services.ArticleService;
import com.byaffe.learningking.services.DashboardService;
import com.byaffe.learningking.services.StudentService;
import com.byaffe.learningking.services.impl.ArticleServiceImpl;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.models.User;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.googlecode.genericdao.search.Search;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Ray Gdhrt
 */
@Slf4j
@RestController
@Hidden
@RequestMapping("api/v1/admin/dashboard")
public class AdminDashboardController {
    @Autowired
    DashboardService dashboardService;
    @Autowired
    StudentService studentService;

    @GetMapping("")
    public ResponseEntity<ResponseObject<DashboardDto>> getById() throws JSONException {
        DashboardDto dto = dashboardService.getSummaries();
        return ResponseEntity.ok().body(new ResponseObject<>(dto));
    }

    @GetMapping("/weekly-counts")
    public ResponseEntity<List<Long>> getDailyCountsForCurrentWeek() {
        List<Long> counts = dashboardService.findWeeklySignupCounts();
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/recent-students")
    public ResponseEntity<ResponseList<Student>> getRecentStudents() {
        List<Student> students = studentService.getStudents(new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE).addSortDesc("id"),0,10);
        return ResponseEntity.ok().body(new ResponseList<>(students, 10, 0, 0));
    }


}
