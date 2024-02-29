package com.byaffe.learningking.services;

import com.byaffe.learningking.dtos.TaskExecutionRequestDTO;
import com.byaffe.learningking.models.TaskExecution;
import com.googlecode.genericdao.search.Search;

import javax.xml.bind.ValidationException;
import java.util.List;

/**
 * Handles CRUD operations on the {@link  TaskExecution}
 */
public interface TaskExecutionService {
    /**
     * Saves a StockEntry to the database
     * @param taskDoerResponseDTO
     * @return
     */
    TaskExecution saveInstance(TaskExecutionRequestDTO taskDoerResponseDTO) throws ValidationException;

    TaskExecution directSave(TaskExecution task) throws ValidationException;

    /**
     *
     * @param id
     * @throws ValidationException
     */
    void deleteInstance(long id) throws ValidationException;

    /**
     * Gets a list of StockEntrys following a supplied search term, offset and limit
     * @param search
     * @return
     */
    List<TaskExecution>getInstances(Search search, int offset, int limit);

    long countInstances(Search search);

     TaskExecution getByTaskId(Long taskId, Long userId) ;
    TaskExecution getByTaskId(Long taskId) ;
    /**
     * Gets a StockEntry that matches a given Id
     * @param id
     * @return
     */
    TaskExecution getInstanceById(long id);

     TaskExecution submit(Long id);

     TaskExecution approve(Long id) ;
     TaskExecution reject(Long id) ;


}
