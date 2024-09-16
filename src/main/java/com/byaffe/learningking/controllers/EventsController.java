package com.byaffe.learningking.controllers;

import com.byaffe.learningking.models.Event;
import com.byaffe.learningking.services.EventService;
import com.byaffe.learningking.services.impl.EventServiceImpl;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
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
@RequestMapping("api/v1/events")
public class EventsController {
    @Autowired
    ModelMapper modelMapper;
    

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<Event>> getById(@PathVariable(name = "id") long id) throws JSONException {
       Event event=ApplicationContextProvider.getBean(EventService.class).getInstanceByID(id);
        return ResponseEntity.ok().body(new ResponseObject<>(event));

    }
    @GetMapping("")
    public ResponseEntity<ResponseList<Event>> getEvents(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                         @RequestParam(value = "offset", required = true) Integer offset,
                                                         @RequestParam(value = "limit", required = true) Integer limit,
                                                         @RequestParam(value = "sortBy", required = false) String sortBy,
                                                         @RequestParam(value = "sortDescending", required = false) Boolean sortDescending,
                                                         @RequestParam(value = "featured", required = false) Boolean featured) throws JSONException {

        Search search = EventServiceImpl.generateSearchObjectForEvents(searchTerm);
        if (featured != null) {
            search.addFilterEqual("isFeatured", featured);
        }
        if (sortBy != null) {
            search.addSort(sortBy, sortDescending);
        }
        List<Event> events = ApplicationContextProvider.getBean(EventService.class).getInstances(search, offset, limit);
        long count = ApplicationContextProvider.getBean(EventService.class).countInstances(search);
        return ResponseEntity.ok().body(new ResponseList<>(events, (int) count, offset, limit));

    }




}
