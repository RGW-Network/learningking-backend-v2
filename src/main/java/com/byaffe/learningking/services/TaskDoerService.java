package com.byaffe.learningking.services;

import com.byaffe.learningking.dtos.TaskDoerResponseDTO;
import com.byaffe.learningking.models.TaskDoer;
import com.googlecode.genericdao.search.Search;

import javax.xml.bind.ValidationException;
import java.util.List;

/**
 * Handles CRUD operations on the {@link  TaskDoer}
 */
public interface TaskDoerService {
    /**
     * Saves a StockEntry to the database
     * @param taskDoerResponseDTO
     * @return
     */
    TaskDoer saveInstance(TaskDoerResponseDTO taskDoerResponseDTO) throws ValidationException;



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
    List<TaskDoer>getInstances(Search search, int offset, int limit);

    long countInstances(Search search);


    /**
     * Gets a StockEntry that matches a given Id
     * @param id
     * @return
     */
    TaskDoer getInstanceById(long id);


}
