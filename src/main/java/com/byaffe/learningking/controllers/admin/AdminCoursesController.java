package com.byaffe.learningking.controllers.admin;

import com.byaffe.learningking.dtos.articles.ArticlesFilterDTO;
import com.byaffe.learningking.dtos.courses.*;
import com.byaffe.learningking.models.courses.*;
import com.byaffe.learningking.services.*;
import com.byaffe.learningking.services.impl.CourseServiceImpl;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.byaffe.learningking.shared.constants.RecordStatus;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ray Gdhrt
 */
@Slf4j
@RestController
@Hidden
@RequestMapping("api/v1/admin/courses")
public class AdminCoursesController {
@Autowired
    ModelMapper modelMapper;
    @PostMapping("")
    public ResponseEntity<ResponseObject<Course>> addCourse(@RequestBody CourseRequestDTO dto) throws JSONException {
Course course=ApplicationContextProvider.getBean(CourseService.class).saveInstance(dto);
        return ResponseEntity.ok().body(new ResponseObject<>(course));
    }
    @PostMapping(path = "/multipart", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BaseResponse> uploadCSV(@RequestPart  CourseRequestDTO dto, @RequestPart(value = "file",required = false) MultipartFile file)  {
      dto.setCoverImage(file);
         ApplicationContextProvider.getBean(CourseService.class).saveInstance(dto);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }
    @PostMapping("/{id}/publish")
    public ResponseEntity<BaseResponse> publishCourse(@PathVariable long id) throws JSONException {
        Course course=ApplicationContextProvider.getBean(CourseService.class).getInstanceByID(id);
        ApplicationContextProvider.getBean(CourseService.class).activatePlan(course);
        return ResponseEntity.ok().body(new BaseResponse(true));

    }
    @PostMapping("/{id}/unpublish")
    public ResponseEntity<BaseResponse> unPublishCourse(@PathVariable long id) throws JSONException {
        Course course=ApplicationContextProvider.getBean(CourseService.class).getInstanceByID(id);
        ApplicationContextProvider.getBean(CourseService.class).deActivatePlan(course);
        return ResponseEntity.ok().body(new BaseResponse(true));

    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<CourseResponseDTO>> getById(@PathVariable(name = "id") long id) throws JSONException {
       System.out.println("ID======="+id);
        Course course=ApplicationContextProvider.getBean(CourseService.class).getInstanceByID(id);
        return ResponseEntity.ok().body(new ResponseObject<>(modelMapper.map(course, CourseResponseDTO.class)));

    }
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<BaseResponse> deleteCourse(@PathVariable long id) throws JSONException {
        Course course=ApplicationContextProvider.getBean(CourseService.class).getInstanceByID(id);
        ApplicationContextProvider.getBean(CourseService.class).deleteInstance(course);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }
    @GetMapping("")
    public ResponseEntity<ResponseList<Course>> getCourses(ArticlesFilterDTO queryParamModel) throws JSONException {

        Search search = CourseServiceImpl.generateSearchObjectForCourses(queryParamModel.getSearchTerm())
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        if (queryParamModel.getCategoryId() != null) {
            search.addFilterEqual("category.id", queryParamModel.getCategoryId());
        }

        if (queryParamModel.getFeatured() != null) {
            search.addFilterEqual("isFeatured", queryParamModel.getFeatured());
        }

        if (queryParamModel.getSortBy() != null) {
            search.addSort(queryParamModel.getSortBy(), queryParamModel.getSortDescending());
        }
        List<Course> courses = ApplicationContextProvider.getBean(CourseService.class).getInstances(search, queryParamModel.getOffset(), queryParamModel.getLimit());
        long count = ApplicationContextProvider.getBean(CourseService.class).countInstances(search);


        return ResponseEntity.ok().body(new ResponseList<>(courses, (int) count, queryParamModel.getOffset(), queryParamModel.getLimit()));

    }

    @GetMapping("/v2/{id}")
    public ResponseEntity<ResponseObject<CourseDetailsResponseDTO>> getCourseById(@PathVariable("id") Long id) throws JSONException {
         Course course = ApplicationContextProvider.getBean(CourseService.class).getInstanceByID(id);
        CourseDetailsResponseDTO responseDTO = new CourseDetailsResponseDTO();
        CourseResponseDTO courseObj = (CourseResponseDTO) course;
        List<CourseLesson> lessons = ApplicationContextProvider.getBean(CourseLessonService.class).getInstances(new Search()
                .addFilterEqual("course", course)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE), 0, 0);
        log.info("Lessons got: {}", lessons.stream().map(r->r.id).toArray());

        double rattings = 1;
        try {
            rattings = ApplicationContextProvider.getBean(CourseRatingService.class).getTotalCourseRatings(ReviewType.COURSE, course.getId()) / 5;
        } catch (Exception e) {
            e.printStackTrace();
        }

        courseObj.setAverageRating(rattings / 5);
        courseObj.setTestimonials(course.getTestimonials());
        courseObj.setLessons(lessons.stream().map(r->modelMapper.map(r, LessonResponseDTO.class)).collect(Collectors.toList()));
        courseObj.setNumberOfLessons(lessons.size());
        responseDTO.setCourse(courseObj);
        return ResponseEntity.ok().body(new ResponseObject<>(responseDTO));
    }



}
