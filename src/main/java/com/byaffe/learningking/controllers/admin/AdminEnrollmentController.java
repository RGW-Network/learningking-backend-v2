package com.byaffe.learningking.controllers.admin;

import com.byaffe.learningking.models.courses.CourseEnrollment;
import com.byaffe.learningking.services.CourseEnrollmentService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Ray Gdhrt
 */
@Slf4j
@RestController
@RequestMapping("api/v1/admin/enrollments")
public class AdminEnrollmentController {
@Autowired
    ModelMapper modelMapper;

@Autowired
CourseEnrollmentService modelService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<CourseEnrollment>> getById(@PathVariable(name = "id") long id) throws JSONException {
        CourseEnrollment course=modelService.getInstanceByID(id);
        return ResponseEntity.ok().body(new ResponseObject<>(course));

    }
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<BaseResponse> deleteCourse(@PathVariable long id) throws JSONException {
        CourseEnrollment courseEnrollment=modelService.getInstanceByID(id);
        modelService.deleteInstance(courseEnrollment);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }
    @GetMapping("")
    public ResponseEntity<ResponseList<CourseEnrollment>> getRecords(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                                     @RequestParam(value = "offset", required = true) Integer offset,
                                                                     @RequestParam(value = "limit", required = true) Integer limit,
                                                                     @RequestParam(value = "courseId", required = false) Integer courseId,
                                                                     @RequestParam(value = "studentId", required = false) Integer studentId

    ) throws JSONException {

        Search search = CourseServiceImpl.generateSearchObjectForCourses(searchTerm)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE);
      
        if (courseId!= null) {
            search.addFilterEqual("course.id", courseId);
        }
        if (studentId!= null) {
            search.addFilterEqual("student.id",studentId);
        }
        List<CourseEnrollment> courses = modelService.getInstances(search, offset, limit);
        long count = modelService.countInstances(search);
        return ResponseEntity.ok().body(new ResponseList<>(courses, (int) count,offset,limit));

    }




}
