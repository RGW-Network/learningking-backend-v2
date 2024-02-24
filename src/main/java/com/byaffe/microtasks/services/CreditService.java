package com.byaffe.microtasks.services;

import com.byaffe.microtasks.dtos.TaskRequestDTO;
import com.byaffe.microtasks.models.Task;
import com.googlecode.genericdao.search.Search;

import javax.xml.bind.ValidationException;
import java.util.List;

/**
 * Handles CRUD operations on the {@link  Task}
 */
public interface CreditService {

    void updateUserCredit(Long id) ;



}
