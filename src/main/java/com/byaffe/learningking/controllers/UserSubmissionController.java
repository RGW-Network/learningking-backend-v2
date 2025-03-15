package com.byaffe.learningking.controllers;

import com.byaffe.learningking.dtos.UserSubmissionRequestDto;
import com.byaffe.learningking.dtos.articles.EventRequestDTO;
import com.byaffe.learningking.models.Event;
import com.byaffe.learningking.models.SubmissionStatus;
import com.byaffe.learningking.models.SubmissionType;
import com.byaffe.learningking.models.UserSubmission;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.services.EventService;
import com.byaffe.learningking.services.UserSubmissionService;
import com.byaffe.learningking.services.impl.EventServiceImpl;
import com.byaffe.learningking.services.impl.UserSubmissionServiceImpl;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Ray Gdhrt
 */
@Slf4j
@RestController
@RequestMapping("api/v1/user-submissions")
public class UserSubmissionController {
    @Autowired
    UserSubmissionService userSubmissionService;

    private final ObjectMapper objectMapper = new ObjectMapper(); // For JSON conversion


    @PostMapping("")
    public ResponseEntity<ResponseObject<UserSubmission>> add(@RequestBody UserSubmissionRequestDto dto, HttpServletRequest httpServletRequest) throws JSONException {
        dto.setRequestDetails(formatRequestorLog(httpServletRequest));
        return ResponseEntity.ok().body(new ResponseObject<>(userSubmissionService.save(dto)));
    }


    private String formatRequestorLog(HttpServletRequest request) {
        try {// Collect request details into a Map
            Map<String, Object> requestDetails = new HashMap<>();
            requestDetails.put("ipAddress", request.getRemoteAddr());
            requestDetails.put("userAgent", request.getHeader("User-Agent"));
            requestDetails.put("referer", request.getHeader("Referer"));
            requestDetails.put("origin", request.getHeader("Origin"));
            requestDetails.put("host", request.getHeader("Host"));
            requestDetails.put("requestMethod", request.getMethod());
            requestDetails.put("queryParams", request.getQueryString());

            // Store headers as a JSON object
            Map<String, String> headers = Collections.list(request.getHeaderNames())
                    .stream()
                    .collect(Collectors.toMap(name -> name, request::getHeader));
            requestDetails.put("headers", headers);


            // Convert request details to JSON string
            return objectMapper.writeValueAsString(requestDetails);
        } catch (Exception e) {
            log.error("Error serializing request details: ", e);
            return "{}"; // Fallback to empty JSON
        }

    }
}
