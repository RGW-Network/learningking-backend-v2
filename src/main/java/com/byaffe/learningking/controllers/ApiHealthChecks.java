package com.byaffe.learningking.controllers;

import com.byaffe.learningking.shared.api.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/health/check")
public class ApiHealthChecks {

    private static final Logger LOGGER = Logger.getLogger(ApiHealthChecks.class.getName());

    @GetMapping("/")
    public ResponseEntity<BaseResponse> isAppAlive() {
        return ResponseEntity.ok().body(new BaseResponse(true));
    }

}
