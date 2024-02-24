package com.byaffe.microtasks.controllers;

import com.byaffe.microtasks.dtos.TaskExecutionRequestDTO;
import com.byaffe.microtasks.dtos.TaskExecutionResponseDTO;
import com.byaffe.microtasks.dtos.TaskRequestDTO;
import com.byaffe.microtasks.dtos.TaskResponseDTO;
import com.byaffe.microtasks.models.Task;
import com.byaffe.microtasks.models.TaskExecution;
import com.byaffe.microtasks.services.TaskExecutionService;
import com.byaffe.microtasks.services.TaskService;
import com.byaffe.microtasks.services.impl.TaskServiceImpl;
import com.byaffe.microtasks.shared.api.BaseResponse;
import com.byaffe.microtasks.shared.api.ResponseList;
import com.googlecode.genericdao.search.Search;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/task-executions")
public class TaskExecutionController {

    @Autowired
    TaskExecutionService taskService;


    @PostMapping("")
    public ResponseEntity<TaskExecutionResponseDTO> attemptTask(@RequestBody TaskExecutionRequestDTO dto) throws ValidationException {
        TaskExecution distributor = taskService.saveInstance(dto);

        TaskExecutionResponseDTO responseDTO = TaskExecutionResponseDTO.fromModel(distributor,false);

        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskExecutionResponseDTO> getById(@PathVariable(value = "id", required = true) Long id) throws ValidationException {
        TaskExecutionResponseDTO responseDTO = TaskExecutionResponseDTO.fromModel(taskService.getInstanceById(id),true);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<BaseResponse> approveTask(@PathVariable(value = "id", required = true) Long id) throws ValidationException {
        taskService.approve(id);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }
    @PostMapping("/{id}/reject")
    public ResponseEntity<BaseResponse> rejectTask(@PathVariable(value = "id", required = true) Long id) throws ValidationException {
        taskService.reject(id);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<BaseResponse> submitTask(@PathVariable(value = "id", required = true) Long id) throws ValidationException {
        taskService.submit(id);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteTask(@PathVariable(value = "id", required = true) long id) throws ValidationException {
        taskService.deleteInstance(id);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }


    @GetMapping("")
    public ResponseEntity<ResponseList<TaskExecutionResponseDTO>> getTasks(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                                  @RequestParam(value = "offset", required = false) Integer offset,
                                                                  @RequestParam(value = "limit", required = false) Integer limit,
                                                                           @RequestParam(value = "taskId", required = false) Long taskId) {
        Search search = TaskServiceImpl.composeSearchObject(searchTerm);
if(taskId!=null){
    search.addFilterEqual("task.id",taskId);
}
        if(!UserDetailsContext.getLoggedInUser().hasAdministrativePrivileges()){
            search.addFilterEqual("createdById", UserDetailsContext.getLoggedInUser().id);
        }
        long totalItems = taskService.countInstances(search);

        List<TaskExecution> models = taskService.getInstances(search, offset, limit);
        List<TaskExecutionResponseDTO> dtos = models.stream().map((TaskExecution dbModel) -> TaskExecutionResponseDTO.fromModel(dbModel,false)).collect(Collectors.toList());
        return ResponseEntity.ok().body(new ResponseList<>(dtos, (int) totalItems, offset, limit));

    }


}
