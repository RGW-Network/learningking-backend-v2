package com.byaffe.learningking.services;

import com.byaffe.learningking.dtos.courses.EventRequestDTO;
import com.byaffe.learningking.models.Event;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

/**
 * Responsible for CRUD operations on {@link Event}
 *
 * @author RayGdhrt
 *
 */
public interface EventService extends GenericService<Event> {


    Event save(EventRequestDTO dto) throws ValidationFailedException;


    Event activate(long plan) throws ValidationFailedException;

    /**
     *
     * @param plan
     * @return
     */
    Event deActivate(long plan);

    /**
     *
     * @param planTitle
     * @return
     */
    Event getByTitle(String planTitle);
    Event getById(Long id);


}
