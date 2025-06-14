package com.byaffe.learningking.controllers.admin;

import com.byaffe.learningking.constants.AccountStatus;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.services.StudentService;
import com.byaffe.learningking.services.impl.StudentServiceImpl;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.byaffe.learningking.shared.constants.PermissionConstant;
import com.byaffe.learningking.shared.security.SessionContext;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.googlecode.genericdao.search.Search;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
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
@RequestMapping("api/v1/admin/students")
public class AdminStudentController {
@Autowired
    StudentService studentService;


    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<Student>> getById(@PathVariable(name = "id") long id) throws JSONException {
        SessionContext.permissionProtection(PermissionConstant.Manage_Students);  Student student=ApplicationContextProvider.getBean(StudentService.class).getStudentById(id);
        return ResponseEntity.ok().body(new ResponseObject<>(student));

    }
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<BaseResponse> deleteStudent(@PathVariable long id) throws JSONException {
        SessionContext.permissionProtection(PermissionConstant.Manage_Students);   Student student=ApplicationContextProvider.getBean(StudentService.class).getInstanceByID(id);
        ApplicationContextProvider.getBean(StudentService.class).deleteInstance(student);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }
    @GetMapping("")
    public ResponseEntity<ResponseList<Student>> getStudents(
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "status", required = false) AccountStatus status,
            @RequestParam(value = "offset", required = true) Integer offset,
            @RequestParam(value = "limit", required = true) Integer limit) throws JSONException {
        SessionContext.permissionProtection(PermissionConstant.Manage_Students);
        Search search = StudentServiceImpl.generateSearchTermsForStudents(searchTerm);

        if (status != null) {
            search.addFilterEqual("accountStatus", status);
        }

        List<Student> students = studentService.getInstances(search, offset, limit);
        long count = studentService.countInstances(search);
        return ResponseEntity.ok().body(new ResponseList<>(students, (int) count, offset, limit));

    }




}
