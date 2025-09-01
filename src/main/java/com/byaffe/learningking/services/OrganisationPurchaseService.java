package com.byaffe.learningking.services;

import com.byaffe.learningking.models.courses.OrganisationPurchase;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;


/**
 * @author Mzee Sr.
 *
 */
public interface OrganisationPurchaseService extends GenericService<OrganisationPurchase> {
     OrganisationPurchase saveInstance(OrganisationPurchase instance) throws ValidationFailedException, OperationFailedException ;

}
