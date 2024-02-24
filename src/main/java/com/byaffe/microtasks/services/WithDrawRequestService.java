package com.byaffe.microtasks.services;

import com.byaffe.microtasks.controllers.UserDetailsContext;
import com.byaffe.microtasks.dtos.WithdrawRequestDTO;
import com.byaffe.microtasks.models.WithdrawRequest;
import com.byaffe.microtasks.models.WithdrawRequestStatus;
import com.byaffe.microtasks.shared.exceptions.ValidationFailedException;
import com.googlecode.genericdao.search.Search;

import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Handles CRUD operations on the {@link  WithdrawRequest}
 */
public interface WithDrawRequestService {
    /**
     * Saves a StockEntry to the database
     * @param taskDoerResponseDTO
     * @return
     */
    WithdrawRequest saveInstance(WithdrawRequestDTO taskDoerResponseDTO) throws ValidationException;



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
    List<WithdrawRequest>getInstances(Search search, int offset, int limit);

    long countInstances(Search search);


    /**
     * Gets a StockEntry that matches a given Id
     * @param id
     * @return
     */
    WithdrawRequest getInstanceById(long id);

     WithdrawRequest disburseWithdrawRequest(Long id) ;

     WithdrawRequest rejectWithdrawRequest(Long id) ;



     WithdrawRequest cancelWithdrawRequest(Long id);


}
