package com.byaffe.learningking.controllers.admin;

import com.byaffe.learningking.controllers.constants.ApiUtils;
import com.byaffe.learningking.controllers.dtos.ArticlesFilterDTO;
import com.byaffe.learningking.controllers.dtos.CourseDetailsResponseDTO;
import com.byaffe.learningking.controllers.dtos.CourseRatingResponseDTO;
import com.byaffe.learningking.controllers.dtos.CourseResponseDTO;
import com.byaffe.learningking.dtos.courses.CourseRequestDTO;
import com.byaffe.learningking.dtos.courses.LessonRequestDTO;
import com.byaffe.learningking.dtos.courses.LessonResponseDTO;
import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseLesson;
import com.byaffe.learningking.models.courses.CourseRating;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.services.CourseLessonService;
import com.byaffe.learningking.services.CourseRatingService;
import com.byaffe.learningking.services.CourseService;
import com.byaffe.learningking.services.impl.CourseServiceImpl;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.googlecode.genericdao.search.Search;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ray Gdhrt
 */
@Slf4j
@RestController
@RequestMapping("api/v1/admin/lessons")
public class AdminLessonsController {
@Autowired
    ModelMapper modelMapper;

@Autowired
CourseLessonService modelService;

    @PostMapping("")
    public ResponseEntity<BaseResponse> saveAndUpdate(@RequestBody LessonRequestDTO dto) throws JSONException {
       modelService.saveInstance(dto);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }
    @PostMapping(path = "/multipart", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BaseResponse> saveAndUpdateV2(@RequestPart @Valid LessonRequestDTO dto, @RequestPart(value = "file",required = false) MultipartFile file) throws JSONException {
      if(file!=null) {
          dto.setCoverImage(file);
      }
        modelService.saveInstance(dto);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<LessonResponseDTO>> getById(@PathVariable(name = "id") long id) throws JSONException {
        CourseLesson course=modelService.getInstanceByID(id);
        return ResponseEntity.ok().body(new ResponseObject<>(modelMapper.map(course, LessonResponseDTO.class)));

    }
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<BaseResponse> deleteCourse(@PathVariable long id) throws JSONException {
        CourseLesson course=modelService.getInstanceByID(id);
        modelService.deleteInstance(course);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }
    @GetMapping("")
    public ResponseEntity<ResponseList<LessonResponseDTO>> getRecords(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                                      @RequestParam(value = "offset", required = true) Integer offset,
                                                                      @RequestParam(value = "limit", required = true) Integer limit,
                                                                      @RequestParam(value = "courseId", required = false) Integer courseId) throws JSONException {

        Search search = CourseServiceImpl.generateSearchObjectForCourses(searchTerm) .addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        if (courseId != null) {
            search.addFilterEqual("course.id", courseId);
        }

        List<CourseLesson> courses = modelService.getInstances(search, offset,limit);
        long count = modelService.countInstances(search);
        return ResponseEntity.ok().body(new ResponseList<>(courses.stream().map(r->modelMapper.map(r,LessonResponseDTO.class)).collect(Collectors.toList()), (int) count, offset, limit));

    }

    @GetMapping("/v2/{id}")
    public ResponseEntity<ResponseObject<LessonResponseDTO>> getById(@PathVariable("id") Long id) throws JSONException {
         CourseLesson course = modelService.getInstanceByID(id);
        return ResponseEntity.ok().body(new ResponseObject<>(modelMapper.map(course, LessonResponseDTO.class)));
    }



}
