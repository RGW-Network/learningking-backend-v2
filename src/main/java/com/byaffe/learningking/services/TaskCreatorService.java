package com.byaffe.learningking.services;

import com.byaffe.learningking.dtos.TaskCreatorResponseDTO;
import com.byaffe.learningking.models.TaskCreator;
import com.googlecode.genericdao.search.Search;

import javax.xml.bind.ValidationException;
import java.util.List;

/**
 * Handles CRUD operations on the {@link  TaskCreator}
 */
public interface TaskCreatorService {
    /**
     * Saves a StockEntry to the database
     * @param taskCreatorResponseDTO
     * @return
     */
    TaskCreator saveInstance(TaskCreatorResponseDTO taskCreatorResponseDTO) throws ValidationException;



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
    List<TaskCreator>getInstances(Search search, int offset, int limit);

    long countInstances(Search search);


    /**
     * Gets a StockEntry that matches a given Id
     * @param id
     * @return
     */
    TaskCreator getInstanceById(long id);


}
