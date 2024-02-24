package com.byaffe.microtasks.controllers;

import com.byaffe.microtasks.dtos.TaskRequestDTO;
import com.byaffe.microtasks.dtos.TaskResponseDTO;
import com.byaffe.microtasks.models.Task;
import com.byaffe.microtasks.models.TaskExecution;
import com.byaffe.microtasks.models.TaskStatus;
import com.byaffe.microtasks.services.TaskExecutionService;
import com.byaffe.microtasks.services.TaskService;
import com.byaffe.microtasks.services.impl.TaskServiceImpl;
import com.byaffe.microtasks.shared.api.BaseResponse;
import com.byaffe.microtasks.shared.api.ResponseList;
import com.googlecode.genericdao.search.Search;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/tasks")
public class TasksController {

    @Autowired
    TaskService taskService;
    @Autowired
    TaskExecutionService taskExecutionService;


    @PostMapping("")
    public ResponseEntity<TaskResponseDTO> saveTask(@RequestBody TaskRequestDTO dto) throws ValidationException {
        Task distributor = taskService.saveInstance(dto);

        TaskResponseDTO responseDTO = TaskResponseDTO.fromModel(distributor,false);

        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable(value = "id", required = true) Long id) throws ValidationException {
        Task model = taskService.getInstance(new Search().addFilterEqual("id",id)
                .addFetch("bigDetails")

        );

            TaskResponseDTO dto = TaskResponseDTO.fromModel(model,true);
            dto.setCurrentExecutions(taskExecutionService.countInstances( new Search().addFilterEqual("task.id", model.id)));
            TaskExecution taskExecution = taskExecutionService.getByTaskId(model.getId(), UserDetailsContext.getLoggedInUser().id);
            if (taskExecution != null) {
                dto.setAlreadyAttempted(true);
                dto.setAttemptStatus(taskExecution.getStatus().getUiName());
                if (taskExecution.getBigDetails() != null){
                    dto.setDetails(taskExecution.getBigDetails().getData());
                }
              //  dto.setAttemptResponse(taskExecution.getDetails());
            }


        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<BaseResponse> activateTask(@PathVariable(value = "id", required = true) Long id) throws ValidationException {
        taskService.activateTask(id);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<BaseResponse> submitTask(@PathVariable(value = "id", required = true) Long id) throws ValidationException {
        taskService.submitTask(id);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<BaseResponse> cancelTask(@PathVariable(value = "id", required = true) Long id) throws ValidationException {
        taskService.cancelTask(id);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteTask(@PathVariable(value = "id", required = true) long id) throws ValidationException {
        taskService.deleteInstance(id);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }


    @GetMapping("")
    public ResponseEntity<ResponseList<TaskResponseDTO>> getTasks(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                                  @RequestParam(value = "offset", required = false) Integer offset,
                                                                  @RequestParam(value = "limit", required = false) Integer limit,
                                                                  @RequestParam(value = "sortBy", required = false) String sortBy,
                                                                  @RequestParam(value = "sortDescending", required = false) Boolean sortDescending,
                                                                  @RequestParam(value = "toAttempt", required = false,defaultValue = "true") Boolean toAttempt) {
        Search search = TaskServiceImpl.composeSearchObject(searchTerm);

        if(toAttempt){
            search.addFilterEqual("status", TaskStatus.PAID_ACTIVE);
        }else{

            if(!UserDetailsContext.getLoggedInUser().hasAdministrativePrivileges()){
                search.addFilterEqual("createdById", UserDetailsContext.getLoggedInUser().id);
            }
        }

        if(StringUtils.isNotEmpty(sortBy)){
            search.addSort(sortBy,sortDescending!=null?sortDescending:true);
        }

        long totalItems = taskService.countInstances(search);

        List<Task> models = taskService.getInstances(search, offset, limit);
        List<TaskResponseDTO> dtos = models.stream().map(model ->
                {
                    TaskResponseDTO dto = TaskResponseDTO.fromModel(model,false);
                    dto.setCurrentExecutions(taskExecutionService.countInstances( new Search().addFilterEqual("task.id", model.id)));
                  return dto;
                }
        ).collect(Collectors.toList());
        return ResponseEntity.ok().body(new ResponseList<>(dtos, (int) totalItems, offset, limit));

    }


}
