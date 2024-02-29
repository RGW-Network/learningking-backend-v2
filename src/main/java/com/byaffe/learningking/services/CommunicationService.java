package com.byaffe.learningking.services;

import com.byaffe.learningking.models.Communication;
import org.sers.webutils.model.exception.OperationFailedException;
com.byaffe.learningking.shared.exceptions.ValidationFailedException;

/**
 * Responsible for CRUD operations on {@link Communication}
 *
 * @author RayGdhrt
 *
 */
public interface CommunicationService extends GenericService<Communication> {

    /**
     * 
     */
    public void sendCommunications();
    
    /**
     * 
     * @param communication
     * @param instant
     * @return
     * @throws ValidationFailedException
     * @throws OperationFailedException 
     */
   Communication saveInstance(Communication communication, boolean instant) throws ValidationFailedException, OperationFailedException;


}
