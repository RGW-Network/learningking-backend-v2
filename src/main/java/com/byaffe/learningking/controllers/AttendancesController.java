package com.byaffe.learningking.controllers;

import com.byaffe.learningking.models.EventAttendance;
import com.byaffe.learningking.models.EventAttendanceStatus;
import com.byaffe.learningking.services.EventAttendanceService;
import com.byaffe.learningking.services.impl.EventAttendanceServiceImpl;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.byaffe.learningking.shared.security.SessionContext;
import com.googlecode.genericdao.search.Search;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Ray Gdhrt
 */
@Slf4j
@RestController
@RequestMapping("api/v1/event-attendances")
public class AttendancesController {
    @Autowired
    EventAttendanceService eventAttendanceService;


    @PostMapping("/{eventId}/attend")
    public ResponseEntity<ResponseObject<EventAttendance>> attend(@PathVariable("eventId") Long eventId) throws JSONException {
        return ResponseEntity.ok().body(new ResponseObject<>(eventAttendanceService.attendFreeEvent(eventId, SessionContext.getLoggedInStudent().getId())));
    }
    @PostMapping("/{eventAttendanceId}/cancel")
    public ResponseEntity<BaseResponse> cancel(@PathVariable("eventAttendanceId") Long eventAttendanceId, @RequestBody String notes) throws JSONException {
          eventAttendanceService.cancel(eventAttendanceId, notes);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }

    @GetMapping("")
    public ResponseEntity<ResponseList<EventAttendance>> getAttendancies(
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "status", required = false) EventAttendanceStatus status,
            @RequestParam(value = "eventId", required = false) Long eventId,
            @RequestParam(value = "offset", required = true) Integer offset,
            @RequestParam(value = "limit", required = true) Integer limit) throws JSONException {
        Search search = EventAttendanceServiceImpl.generateSearchTerms(searchTerm);
        if (status != null) {
            search.addFilterEqual("status", status);
        }
        if (eventId != null) {
            search.addFilterEqual("event.id", eventId);
        }
        if (SessionContext.getLoggedInStudent()!=null) {
            search.addFilterEqual("student.id", SessionContext.getLoggedInStudent().getId());
        }
        List<EventAttendance> records = eventAttendanceService.getInstances(search, offset, limit);
        long count = eventAttendanceService.countInstances(search);
        return ResponseEntity.ok().body(new ResponseList<>(records, count, offset, limit));

    }





}
