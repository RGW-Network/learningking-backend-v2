package com.byaffe.microtasks.controllers;

import com.byaffe.microtasks.dtos.TaskExecutionRequestDTO;
import com.byaffe.microtasks.dtos.TaskExecutionResponseDTO;
import com.byaffe.microtasks.dtos.WithdrawRequestDTO;
import com.byaffe.microtasks.dtos.WithdrawRequestResponseDTO;
import com.byaffe.microtasks.models.TaskExecution;
import com.byaffe.microtasks.models.WithdrawRequest;
import com.byaffe.microtasks.services.TaskExecutionService;
import com.byaffe.microtasks.services.WithDrawRequestService;
import com.byaffe.microtasks.services.impl.TaskServiceImpl;
import com.byaffe.microtasks.services.impl.WithdrawRequestServiceImpl;
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
@RequestMapping("/api/v1/withdraw-requests")
public class WithdrawRequestController {

    @Autowired
    WithDrawRequestService taskService;


    @PostMapping("")
    public ResponseEntity<WithdrawRequestResponseDTO> attemptTask(@RequestBody WithdrawRequestDTO dto) throws ValidationException {
        WithdrawRequest distributor = taskService.saveInstance(dto);

        WithdrawRequestResponseDTO responseDTO = WithdrawRequestResponseDTO.fromModel(distributor);

        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WithdrawRequestResponseDTO> getById(@PathVariable(value = "id", required = true) Long id) throws ValidationException {
        WithdrawRequestResponseDTO responseDTO = WithdrawRequestResponseDTO.fromModel(taskService.getInstanceById(id));
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("/{id}/disburse")
    public ResponseEntity<BaseResponse> disburse(@PathVariable(value = "id", required = true) Long id) throws ValidationException {
        taskService.disburseWithdrawRequest(id);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<BaseResponse> cancelTask(@PathVariable(value = "id", required = true) Long id) throws ValidationException {
        taskService.cancelWithdrawRequest(id);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteTask(@PathVariable(value = "id", required = true) long id) throws ValidationException {
        taskService.deleteInstance(id);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }


    @GetMapping("")
    public ResponseEntity<ResponseList<WithdrawRequestResponseDTO>> getTasks(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                                  @RequestParam(value = "offset", required = false) Integer offset,
                                                                  @RequestParam(value = "limit", required = false) Integer limit) {
        Search search = WithdrawRequestServiceImpl.composeSearchObject(searchTerm);

        long totalItems = taskService.countInstances(search);

        List<WithdrawRequest> models = taskService.getInstances(search, offset, limit);
        List<WithdrawRequestResponseDTO> dtos = models.stream().map(WithdrawRequestResponseDTO::fromModel).collect(Collectors.toList());
        return ResponseEntity.ok().body(new ResponseList<>(dtos, (int) totalItems, offset, limit));

    }


}
