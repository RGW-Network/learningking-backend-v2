package com.byaffe.learningking.controllers.admin;

import com.byaffe.learningking.controllers.dtos.*;
import com.byaffe.learningking.dtos.courses.EventRequestDTO;
import com.byaffe.learningking.models.Event;
import com.byaffe.learningking.services.EventService;
import com.byaffe.learningking.services.impl.EventServiceImpl;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.googlecode.genericdao.search.Search;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ray Gdhrt
 */
@Slf4j
@RestController
@RequestMapping("api/v1/admin/events")
public class AdminEventController {
@Autowired
    ModelMapper modelMapper;
    @PostMapping("")
    public ResponseEntity<ResponseObject<Event>> addEvent(@RequestBody EventRequestDTO dto) throws JSONException {
Event event=ApplicationContextProvider.getBean(EventService.class).save(dto);
        return ResponseEntity.ok().body(new ResponseObject<>(event));

    }
    @PostMapping(path = "/multipart", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BaseResponse> uploadCSV(@RequestPart  EventRequestDTO dto, @RequestPart(value = "file",required = false) MultipartFile file)  {
      dto.setCoverImage(file);
         ApplicationContextProvider.getBean(EventService.class).save(dto);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }
    @PostMapping("/{id}/publish")
    public ResponseEntity<BaseResponse> publishEvent(@PathVariable long id) throws JSONException {
        Event event=ApplicationContextProvider.getBean(EventService.class).getInstanceByID(id);
        ApplicationContextProvider.getBean(EventService.class).activate(id);
        return ResponseEntity.ok().body(new BaseResponse(true));

    }
    @PostMapping("/{id}/unpublish")
    public ResponseEntity<BaseResponse> unPublishEvent(@PathVariable long id) throws JSONException {
        Event event=ApplicationContextProvider.getBean(EventService.class).getInstanceByID(id);
        ApplicationContextProvider.getBean(EventService.class).deActivate(id);
        return ResponseEntity.ok().body(new BaseResponse(true));

    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<Event>> getById(@PathVariable(name = "id") long id) throws JSONException {
       System.out.println("ID======="+id);
        Event event=ApplicationContextProvider.getBean(EventService.class).getById(id);
        return ResponseEntity.ok().body(new ResponseObject<>(event));

    }
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<BaseResponse> deleteEvent(@PathVariable long id) throws JSONException {
        Event event=ApplicationContextProvider.getBean(EventService.class).getInstanceByID(id);
        ApplicationContextProvider.getBean(EventService.class).deleteInstance(event);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }
    @GetMapping("")
    public ResponseEntity<ResponseList<Event>> getEvents(ArticlesFilterDTO queryParamModel) throws JSONException {

        Search search = EventServiceImpl.generateSearchObjectForEvents(queryParamModel.getSearchTerm())
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        if (queryParamModel.getCategoryId() != null) {
            search.addFilterEqual("category.id", queryParamModel.getCategoryId());
        }

        if (queryParamModel.getFeatured() != null) {
            search.addFilterEqual("isFeatured", queryParamModel.getFeatured());
        }

        if (queryParamModel.getSortBy() != null) {
            search.addSort(queryParamModel.getSortBy(), queryParamModel.getSortDescending());
        }
        List<Event> events = ApplicationContextProvider.getBean(EventService.class).getInstances(search, queryParamModel.getOffset(), queryParamModel.getLimit());
        long count = ApplicationContextProvider.getBean(EventService.class).countInstances(search);


        return ResponseEntity.ok().body(new ResponseList<>(events, (int) count, queryParamModel.getOffset(), queryParamModel.getLimit()));

    }




}
