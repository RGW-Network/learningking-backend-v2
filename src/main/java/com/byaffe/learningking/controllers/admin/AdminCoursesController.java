package com.byaffe.learningking.controllers.admin;

import com.byaffe.learningking.dtos.articles.ArticlesFilterDTO;
import com.byaffe.learningking.dtos.courses.*;
import com.byaffe.learningking.models.courses.*;
import com.byaffe.learningking.services.*;
import com.byaffe.learningking.services.impl.CourseServiceImpl;
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
import org.json.JSONException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
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
        SessionContext.permissionProtection(PermissionConstant.Course_Create);
        Course course=ApplicationContextProvider.getBean(CourseService.class).saveInstance(dto);
        return ResponseEntity.ok().body(new ResponseObject<>(course));
    }
    @PostMapping(path = "/multipart", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BaseResponse> uploadCSV(@RequestPart  CourseRequestDTO dto, @RequestPart(value = "file",required = false) MultipartFile file)  {
        SessionContext.permissionProtection(PermissionConstant.Course_Create);if(file!=null) {
         dto.setCoverImage(file);
     }
         ApplicationContextProvider.getBean(CourseService.class).saveInstance(dto);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }
    @PostMapping("/{id}/publish")
    public ResponseEntity<BaseResponse> publishCourse(@PathVariable long id) throws JSONException {
        SessionContext.permissionProtection(PermissionConstant.Course_Publish_Own);  Course course=ApplicationContextProvider.getBean(CourseService.class).getInstanceByID(id);
        ApplicationContextProvider.getBean(CourseService.class).activatePlan(course);
        return ResponseEntity.ok().body(new BaseResponse(true));

    }
    @PostMapping("/{id}/unpublish")
    public ResponseEntity<BaseResponse> unPublishCourse(@PathVariable long id) throws JSONException {
        SessionContext.permissionProtection(PermissionConstant.Course_Publish_Own);  Course course=ApplicationContextProvider.getBean(CourseService.class).getInstanceByID(id);
        ApplicationContextProvider.getBean(CourseService.class).deActivatePlan(course);
        return ResponseEntity.ok().body(new BaseResponse(true));

    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<CourseResponseDTO>> getById(@PathVariable(name = "id") long id) throws JSONException {
       SessionContext.permissionProtection(PermissionConstant.Course_View_Own);
        Course course=ApplicationContextProvider.getBean(CourseService.class).getInstanceByID(id);
        return ResponseEntity.ok().body(new ResponseObject<>(modelMapper.map(course, CourseResponseDTO.class)));

    }
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<BaseResponse> deleteCourse(@PathVariable long id) throws JSONException {
        SessionContext.permissionProtection(PermissionConstant.Course_Delete_All);
        Course course=ApplicationContextProvider.getBean(CourseService.class).getInstanceByID(id);
        ApplicationContextProvider.getBean(CourseService.class).deleteInstance(course);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }
    @GetMapping("")
    public ResponseEntity<ResponseList<Course>> getCourses(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                           @RequestParam(value = "offset", required = true) Integer offset,
                                                           @RequestParam(value = "limit", required = true) Integer limit,
                                                           @RequestParam(value = "sortBy", required = false) String sortBy,
                                                           @RequestParam(value = "sortDescending", required = false) Boolean sortDescending,
                                                           @RequestParam(value = "academyType", required = false) CourseAcademyType academyType,
                                                           @RequestParam(value = "categories", required = false) String commaSeparatedCategories ,
                                                           @RequestParam(value = "publicationStatus", required = false) PublicationStatus publicationStatus,
                                                           @RequestParam(value = "instructors", required = false) String commaSeparatedInstructors) throws JSONException {
        //SessionContext.permissionProtection(PermissionConstant.Course_Create);
        Search search = CourseServiceImpl.generateSearchObjectForCourses(searchTerm)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        if (commaSeparatedCategories!= null) {
           List<Long> ids= Arrays.stream(commaSeparatedCategories.split(",")).map(Long::parseLong).collect(Collectors.toList());
            search.addFilterIn("category.id", ids);
        }
        if (commaSeparatedInstructors!= null) {
            List<Long> ids= Arrays.stream(commaSeparatedInstructors.split(",")).map(Long::parseLong).collect(Collectors.toList());
            search.addFilterIn("instructor.id", ids);
        }

        if (publicationStatus != null) {
            search.addFilterEqual("publicationStatus",publicationStatus);
        }

        if (academyType != null) {
            search.addFilterEqual("academy",academyType);
        }

        if(!SessionContext.isSuperAdmin()){
            search.addFilterEqual("instructor.userAccount.id",SessionContext.getLoggedInUser().getId());
        }
        List<Course> courses = ApplicationContextProvider.getBean(CourseService.class).getInstances(search, offset, limit);
        long count = ApplicationContextProvider.getBean(CourseService.class).countInstances(search);


        return ResponseEntity.ok().body(new ResponseList<>(courses, (int) count, offset, limit));

    }

    @GetMapping("/v2/{id}")
    public ResponseEntity<ResponseObject<CourseDetailsResponseDTO>> getCourseById(@PathVariable("id") Long id) throws JSONException {
        SessionContext.permissionProtection(PermissionConstant.Course_View_Own);
        Course course = ApplicationContextProvider.getBean(CourseService.class).getInstanceByID(id);
        CourseDetailsResponseDTO responseDTO = new CourseDetailsResponseDTO();
        CourseResponseDTO courseObj = (CourseResponseDTO) course;
        List<CourseLesson> lessons = ApplicationContextProvider.getBean(CourseLessonService.class).getInstances(new Search()
                .addFilterEqual("course", course)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE), 0, 0);

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
