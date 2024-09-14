package com.byaffe.learningking.controllers.admin;

import com.byaffe.learningking.dtos.courses.CourseTopicRequestDTO;
import com.byaffe.learningking.models.courses.CourseTopic;
import com.byaffe.learningking.services.CourseTopicService;
import com.byaffe.learningking.services.impl.CourseServiceImpl;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.byaffe.learningking.shared.constants.RecordStatus;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ray Gdhrt
 */
@Slf4j
@RestController
@RequestMapping("api/v1/admin/course-topics")
public class AdminTopicsController {
@Autowired
    ModelMapper modelMapper;

@Autowired
CourseTopicService modelService;
    @PostMapping("")
    public ResponseEntity<BaseResponse> saveAndUpdate(@RequestBody CourseTopicRequestDTO dto) throws JSONException {
        modelService.saveInstance(dto);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }
    @PostMapping(path = "/multipart", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BaseResponse> saveAndUpdateV2(@RequestPart @Valid CourseTopicRequestDTO dto, @RequestPart("file") MultipartFile file) throws JSONException {
        dto.setCoverImage(file);
        modelService.saveInstance(dto);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<CourseTopicRequestDTO>> getById(@PathVariable(name = "id") long id) throws JSONException {
        CourseTopic course=modelService.getInstanceByID(id);
        return ResponseEntity.ok().body(new ResponseObject<>(modelMapper.map(course, CourseTopicRequestDTO.class)));

    }
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<BaseResponse> deleteCourse(@PathVariable long id) throws JSONException {
        CourseTopic course=modelService.getInstanceByID(id);
        modelService.deleteInstance(course);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }
    @GetMapping("")
    public ResponseEntity<ResponseList<CourseTopicRequestDTO>> getRecords(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                                          @RequestParam(value = "offset", required = true) Integer offset,
                                                                          @RequestParam(value = "limit", required = true) Integer limit,
                                                                          @RequestParam(value = "courseId", required = false) Integer courseId,
                                                                          @RequestParam(value = "lessonId", required = false) Integer lessonId

    ) throws JSONException {

        Search search = CourseServiceImpl.generateSearchObjectForCourses(searchTerm)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE);
      
        if (courseId!= null) {
            search.addFilterEqual("courseLesson.course.id", courseId);
        }
        if (lessonId!= null) {
            search.addFilterEqual("courseLesson.id",lessonId);
        }
        List<CourseTopic> courses = modelService.getInstances(search, offset, limit);
        long count = modelService.countInstances(search);
        return ResponseEntity.ok().body(new ResponseList<>(courses.stream().map(r->modelMapper.map(r,CourseTopicRequestDTO.class)).collect(Collectors.toList()), (int) count,offset,limit));

    }




}
