package com.byaffe.learningking.controllers;

import com.byaffe.learningking.dtos.auth.*;
import com.byaffe.learningking.dtos.courses.CourseRequestDTO;
import com.byaffe.learningking.dtos.instructor.InstructorRequestDTO;
import com.byaffe.learningking.dtos.instructor.InstructorResponseDTO;
import com.byaffe.learningking.dtos.student.StudentProfileUpdateRequestDTO;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.courses.CourseInstructor;
import com.byaffe.learningking.services.CourseService;
import com.byaffe.learningking.services.InstructorService;
import com.byaffe.learningking.services.StudentService;
import com.byaffe.learningking.services.UserService;
import com.byaffe.learningking.services.impl.InstructorServiceImpl;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.security.SessionContext;
import com.byaffe.learningking.shared.security.TokenProvider;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.googlecode.genericdao.search.Search;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/instructors")
public class InstructorsController {


    @Autowired
    InstructorService instructorService;
    @PostMapping("")
    public ResponseEntity<BaseResponse> updateInstructor(@RequestBody InstructorRequestDTO userDTO) throws ValidationException {
     SessionContext.superAdminProtection();
     instructorService.save(userDTO);
        return ResponseEntity.ok().body(new BaseResponse("Success", true));
    }

    @PostMapping(path = "/multipart", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BaseResponse> uploadCSV(@RequestPart InstructorRequestDTO dto, @RequestPart(value = "profileImage",required = false) MultipartFile profileImage,@RequestPart(value = "coverImage",required = false) MultipartFile coverImage)  {
        SessionContext.superAdminProtection();
        dto.setCoverImage(coverImage);
        dto.setProfileImage(profileImage);
        instructorService.save(dto);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }
    @GetMapping("")
    public ResponseEntity<ResponseList<CourseInstructor>> getInstructors(
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "offset", required = true) Integer offset,
            @RequestParam(value = "limit", required = true) Integer limit
    ) {
        Search search = InstructorServiceImpl.generateSearchObjectForCourses(searchTerm) .addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        long count = instructorService.countInstances(search);
        List<CourseInstructor> courses = instructorService.getInstances(search, offset, limit);
        return ResponseEntity.ok().body(new ResponseList<>(courses, count, offset, limit));
    }
}
