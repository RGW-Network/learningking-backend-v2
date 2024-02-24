package com.byaffe.microtasks.services.impl;

import com.byaffe.microtasks.controllers.UserDetailsContext;
import com.byaffe.microtasks.daos.TaskDao;
import com.byaffe.microtasks.daos.TaskExecutionDao;
import com.byaffe.microtasks.dtos.TaskDoerResponseDTO;
import com.byaffe.microtasks.dtos.TaskExecutionRequestDTO;
import com.byaffe.microtasks.dtos.TaskRequestDTO;
import com.byaffe.microtasks.models.*;
import com.byaffe.microtasks.services.CreditService;
import com.byaffe.microtasks.services.TaskExecutionService;
import com.byaffe.microtasks.services.TaskService;
import com.byaffe.microtasks.shared.constants.RecordStatus;
import com.byaffe.microtasks.shared.exceptions.OperationFailedException;
import com.byaffe.microtasks.shared.exceptions.ResourceNotFoundException;
import com.byaffe.microtasks.shared.exceptions.ValidationFailedException;
import com.byaffe.microtasks.shared.utils.CustomSearchUtils;
import com.byaffe.microtasks.shared.utils.Validate;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class TaskExecutionServiceImpl implements TaskExecutionService {
    @Autowired
    TaskExecutionDao taskExecutionDao;
    @Autowired
    TaskDao taskDao;

@Autowired
    CreditService creditService;
    @Override
    public TaskExecution saveInstance(TaskExecutionRequestDTO dto) {

        Task task = taskDao.findById(dto.getTaskId()).orElseThrow(() -> new OperationFailedException("Task not found"));
        TaskExecution existingTaskExecution = getByTaskId(task.getId(), UserDetailsContext.getLoggedInUser().id);

        if (existingTaskExecution != null) {
            throw new ValidationFailedException("You already attempted this task");
        }

        if (task.getVerificationType().equals(TaskVerificationType.AUTOMATED) && StringUtils.isEmpty(dto.getConfirmationToken())) {
            throw new ValidationFailedException("Confirmation token required for this task");
        }
        if (task.getVerificationType().equals(TaskVerificationType.MANUAL) && (StringUtils.isEmpty(dto.getDetails()))) {
            throw new ValidationFailedException("Confirmation details missing");
        }
        TaskExecution taskExecution = new TaskExecution();

        taskExecution.setTask(task);
        taskExecution.setStatus(TaskExecutionStatus.SUBMITTED);
        taskExecution.setBigDetails(new BigDetails(dto.getDetails()));
        taskExecution.setAmountPaid(task.getCostPerExecution());
        taskExecution.setConfirmationAttachmentUrl(dto.getConfirmationAttachmentUrl());
        taskExecution.setDateCompleted(LocalDateTime.now());
        taskExecution.setConfirmationToken(dto.getConfirmationToken());

        if (task.getVerificationType().equals(TaskVerificationType.AUTOMATED)) {
            taskExecution.setStatus(TaskExecutionStatus.APPROVED);
            if (!task.getAutoApprovalTokenList().contains(dto.getConfirmationToken().trim())) {
                taskExecution.setStatus(TaskExecutionStatus.REJECTED);
                taskExecution.setRejectionReason("Token supplied was invalid");
            }
            if (getUsedToken(task, dto.getConfirmationToken()) != null) {
                taskExecution.setStatus(TaskExecutionStatus.REJECTED);
                taskExecution.setRejectionReason("Token supplied was already used");
            }
        }
        return taskExecutionDao.merge(taskExecution);
    }

    private TaskExecution getUsedToken(Task task, String token) {
        return taskExecutionDao.searchUnique(new Search()
                .addFilterEqual("task", task)
                .addFilterEqual("confirmationToken", token)
                .addFilterEqual("status", TaskExecutionStatus.APPROVED)
                .setMaxResults(1)
        );

    }

    public TaskExecution submit(Long id) {
        TaskExecution withdrawRequest = getInstanceById(id);
        if (UserDetailsContext.getLoggedInUser().id != withdrawRequest.getCreatedById()) {
            throw new ValidationFailedException("You don't have permissions to do this");
        }

        if (!withdrawRequest.getStatus().equals(TaskExecutionStatus.DRAFT)) {
            throw new ValidationFailedException("You can only submit draft attempts");
        }
        withdrawRequest.setStatus(TaskExecutionStatus.SUBMITTED);
        withdrawRequest.setDateCompleted(LocalDateTime.now());
        return taskExecutionDao.save(withdrawRequest);
    }

    public TaskExecution approve(Long id) {
        TaskExecution withdrawRequest = getInstanceById(id);
        if (!UserDetailsContext.getLoggedInUser().hasAdministrativePrivileges()) {
            throw new ValidationFailedException("You don't have permissions to do this");
        }
        if (!withdrawRequest.getStatus().equals(TaskExecutionStatus.SUBMITTED)) {
            throw new ValidationFailedException("You can only approve submitted tasks");
        }
        withdrawRequest.setStatus(TaskExecutionStatus.APPROVED);
        withdrawRequest= taskExecutionDao.save(withdrawRequest);

        creditService.updateUserCredit(withdrawRequest.getCreatedById());
        return withdrawRequest;
    }
    public TaskExecution reject(Long id) {
        TaskExecution withdrawRequest = getInstanceById(id);
        if (!UserDetailsContext.getLoggedInUser().hasAdministrativePrivileges()) {
            throw new ValidationFailedException("You don't have permissions to do this");
        }
        if (!withdrawRequest.getStatus().equals(TaskExecutionStatus.SUBMITTED)) {
            throw new ValidationFailedException("You can only reject submitted tasks");
        }
        withdrawRequest.setStatus(TaskExecutionStatus.REJECTED);
        withdrawRequest.setRejectionReason("Manually rejected by task creator");
        return taskExecutionDao.save(withdrawRequest);
    }

    @Override
    public void deleteInstance(long id) throws ValidationException {
        TaskExecution existsWithId = getInstanceById(id);
        existsWithId.setRecordStatus(RecordStatus.DELETED);
        taskExecutionDao.save(existsWithId);

    }


    @Override
    public List<TaskExecution> getInstances(Search search, int offset, int limit) {
        search.setMaxResults(limit);
        search.setFirstResult(offset);
        return taskExecutionDao.search(search);
    }

    public long countInstances(Search search) {
        return taskExecutionDao.count(search);
    }

    public TaskExecution directSave(TaskExecution task) throws ValidationException{
        return taskExecutionDao.merge(task);
    }
    @Override
    public TaskExecution getInstanceById(long id) {
        return taskExecutionDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
    }

    public TaskExecution getByTaskId(Long taskId, Long userId) {
        return taskExecutionDao.searchUnique(new Search()
                .addFilterEqual("task.id", taskId)
                .addFilterEqual("createdById", userId)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                        .addFetch("bigDetails")
                .setMaxResults(1));


    }

    public TaskExecution getByTaskId(Long taskId) {
        return taskExecutionDao.searchUnique(new Search()
                .addFilterEqual("task.id", taskId)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .setMaxResults(1));


    }

    public static Search composeSearchObject(String searchTerm) {
        Search search = CustomSearchUtils.generateSearchTerms(searchTerm,
                Arrays.asList("remarks", "product.name"));

        return search;
    }

}
