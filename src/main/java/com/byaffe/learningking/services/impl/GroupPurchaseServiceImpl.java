/**
 *
 */
package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.models.courses.OrganisationPurchase;
import com.byaffe.learningking.models.payments.Currency;
import com.byaffe.learningking.services.CurrencyService;
import com.byaffe.learningking.services.OrganisationPurchaseService;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.models.Country;
import com.byaffe.learningking.shared.utils.CustomSearchUtils;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * @author Mzee Sr.
 *
 */
@Service
@Transactional
public class GroupPurchaseServiceImpl extends GenericServiceImpl<OrganisationPurchase> implements OrganisationPurchaseService {


    public static Search generateSearchTerms(String searchTerm) {

        return CustomSearchUtils.generateSearchTerms(searchTerm, Arrays.asList("group.organisation.name", "group.name"));
    }
    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pahappa.systems.core.services.GenericService#
	 * saveInstance(java.lang.Object)
     */
    @Override
    public OrganisationPurchase saveInstance(OrganisationPurchase instance) throws ValidationFailedException, OperationFailedException {
        return super.save(instance);
    }


    @Override
    public boolean isDeletable(OrganisationPurchase entity) throws OperationFailedException {
        return false;
    }
}
