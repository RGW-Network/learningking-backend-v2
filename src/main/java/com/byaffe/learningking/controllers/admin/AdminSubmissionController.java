package com.byaffe.learningking.controllers.admin;

import com.byaffe.learningking.dtos.UserSubmissionRequestDto;
import com.byaffe.learningking.models.SubmissionStatus;
import com.byaffe.learningking.models.SubmissionType;
import com.byaffe.learningking.models.UserSubmission;
import com.byaffe.learningking.services.UserSubmissionService;
import com.byaffe.learningking.services.impl.UserSubmissionServiceImpl;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.genericdao.search.Search;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
@Hidden
@RequestMapping("api/v1/admin/user-submissions")
public class AdminSubmissionController {
    @Autowired
    UserSubmissionService userSubmissionService;

    private final ObjectMapper objectMapper = new ObjectMapper(); // For JSON conversion


    @PostMapping("/{id}/resolve")
    public ResponseEntity<BaseResponse> resolve(@PathVariable long id) throws JSONException {
        userSubmissionService.resolve(id);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }


    @GetMapping("")
    public ResponseEntity<ResponseList<UserSubmission>> getSubmissions(
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "status", required = false) SubmissionStatus status,
            @RequestParam(value = "type", required = false) SubmissionType type,
            @RequestParam(value = "offset", required = true) Integer offset,
            @RequestParam(value = "limit", required = true) Integer limit) throws JSONException {
        Search search = UserSubmissionServiceImpl.generateSearchObjectForUserSubmissions(searchTerm);

        if (status != null) {
            search.addFilterEqual("status", status);
        }

        if (type != null) {
            search.addFilterEqual("type", type);
        }

        List<UserSubmission> events = userSubmissionService.getInstances(search, offset, limit);
        long count = userSubmissionService.countInstances(search);
        return ResponseEntity.ok().body(new ResponseList<>(events, (int) count, offset, limit));
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
