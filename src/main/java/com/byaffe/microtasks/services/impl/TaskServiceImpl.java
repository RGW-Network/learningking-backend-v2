package com.byaffe.microtasks.services.impl;

import com.byaffe.microtasks.controllers.UserDetailsContext;
import com.byaffe.microtasks.daos.TaskDao;
import com.byaffe.microtasks.dtos.TaskRequestDTO;
import com.byaffe.microtasks.models.*;
import com.byaffe.microtasks.services.LookupValueService;
import com.byaffe.microtasks.shared.exceptions.ValidationFailedException;
import com.googlecode.genericdao.search.Search;
import com.byaffe.microtasks.services.TaskService;
import com.byaffe.microtasks.shared.constants.RecordStatus;
import com.byaffe.microtasks.shared.exceptions.ResourceNotFoundException;
import com.byaffe.microtasks.shared.utils.CustomSearchUtils;
import com.byaffe.microtasks.shared.utils.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {
    @Autowired
    TaskDao taskDao;

    @Autowired
    LookupValueService lookupValueService;

    @Override
    public Task saveInstance(TaskRequestDTO dto) {
        Validate.hasText(dto.getName(), "Missing name");
        Validate.hasText(dto.getDetails(), "Missing instructions");
        Validate.notNull(dto.getCostPerExecution(), "Missing cost");
        Validate.notNull(dto.getRequiredExecutions(), "Missing required executions");
        Validate.notNull(dto.getComplexityId(), "Missing complexity");
        Validate.notNull(dto.getVerificationType(), "Missing verification type");


        Task task = new Task();
        if (dto.getId() > 0) {
            task = getInstanceById(dto.getId());

        }
        if (task.isSaved() && !task.getStatus().equals(TaskStatus.DRAFT)) {

            throw new ValidationFailedException("Record not editable");
        }
        task.setId(dto.getId());
        task.setName(dto.getName());
        task.setBigDetails(new BigDetails(dto.getDetails()));
        task.setRequiredExecutions(dto.getRequiredExecutions());
        task.setCostPerExecution(dto.getCostPerExecution());
        task.setAutoApprovalRegex(dto.getAutoApprovalRegex());
        task.setAutoApprovalTokenList(dto.getAutoApprovalTokenList());
        task.setLocality(TaskLocality.getById(dto.getLocalityId()));
        task.setComplexity(TaskComplexity.getById(dto.getComplexityId()));
        task.setVerificationType(TaskVerificationType.getById(dto.getVerificationType()));
task.setAutoApprovalTokenList(dto.getAutoApprovalTokenList());
        return taskDao.merge(task);
    }

    public Task directSave(Task task) throws ValidationException{
        return taskDao.merge(task);
    }
    @Override
    public void deleteInstance(long id) throws ValidationFailedException {
        Task existsWithId = getInstanceById(id);
        existsWithId.setRecordStatus(RecordStatus.DELETED);
        taskDao.save(existsWithId);

    }

    @Override
    public List<Task> getInstances(Search search, int offset, int limit) {
        search.setMaxResults(limit);
        search.setFirstResult(offset);
        return taskDao.search(search);
    }

    public Task getInstance(Search search) {
        search.setMaxResults(1);
        return taskDao.searchUnique(search);
    }

    public long countInstances(Search search) {
        return taskDao.count(search);
    }

    public Task activateTask(Long id) {
        Task task= getInstanceById(id);
        if(!UserDetailsContext.getLoggedInUser().hasAdministrativePrivileges()){
            throw new ValidationFailedException("You don't have permissions to do this");
        }

        if(!task.getStatus().equals(TaskStatus.SUBMITTED)&&!task.getStatus().equals(TaskStatus.DRAFT)) {
           throw new ValidationFailedException("You can only activate submitted tasks");
        }
        task.setStatus(TaskStatus.PAID_ACTIVE);
        return taskDao.save(task);
    }

    public Task submitTask(Long id) {
        Task task= getInstanceById(id);
        if(!task.getStatus().equals(TaskStatus.DRAFT)) {
            throw new ValidationFailedException("You can only submit draft tasks");
        }
        task.setStatus(TaskStatus.SUBMITTED);
        return taskDao.save(task);
    }

    public Task cancelTask(Long id) {
        Task task= getInstanceById(id);
        if(task.getStatus().equals(TaskStatus.COMPLETED)) {
            throw new ValidationFailedException("You cant cancel completed tasks");
        }

        if(task.getStatus().equals(TaskStatus.CANCELLED)) {
            throw new ValidationFailedException("Task already cancelled");
        }
        task.setStatus(TaskStatus.CANCELLED);
        return taskDao.save(task);
    }
    @Override
    public Task getInstanceById(long id) {
        return taskDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
    }


    public static Search composeSearchObject(String searchTerm) {
        Search search = CustomSearchUtils.generateSearchTerms(searchTerm,
                Arrays.asList("name","category.value", "details"));

        return search;
    }

}
