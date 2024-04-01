package com.byaffe.learningking.services;

import com.byaffe.learningking.dtos.TaskRequestDTO;
import com.byaffe.learningking.models.Task;
import com.googlecode.genericdao.search.Search;

import javax.xml.bind.ValidationException;
import java.util.List;

/**
 * Handles CRUD operations on the {@link  Task}
 */
public interface TaskService {
    /**
     * Saves a Task to the database
     * @param dto
     * @return
     */
    Task saveInstance(TaskRequestDTO dto) throws ValidationException;

    Task directSave(Task task) throws ValidationException;

    /**
     *
     * @param id
     * @throws ValidationException
     */
    void deleteInstance(long id) throws ValidationException;

    /**
     * Gets a list of Tasks following a supplied search term, offset and limit
     * @param search
     * @return
     */
    List<Task>getInstances(Search search, int offset, int limit);

    long countInstances(Search search);
    public Task getInstance(Search search) ;

    /**
     * Gets a Task that matches a given Id
     * @param id
     * @return
     */
    Task getInstanceById(long id);

     Task activateTask(Long id) ;
     Task submitTask(Long id);

     Task cancelTask(Long id) ;
}
