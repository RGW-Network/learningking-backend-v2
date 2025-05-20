package com.byaffe.learningking.controllers;

import com.byaffe.learningking.dtos.student.StudentDashboardDto;
import com.byaffe.learningking.shared.api.ResponseObject;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Ray Gdhrt
 */
@Slf4j
@RestController
@RequestMapping("api/v1/student-reports")
public class ReportsController {


    @GetMapping("/dashboard")
    public ResponseEntity<ResponseObject<StudentDashboardDto>> add() throws JSONException {
        return ResponseEntity.ok().body(new ResponseObject<>(new StudentDashboardDto()));
    }



}
