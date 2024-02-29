/**
 *
 */
package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.models.payments.Currency;
import com.byaffe.learningking.services.CurrencyService;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.models.Country;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Mzee Sr.
 *
 */
@Service
@Transactional
public class CurrencyServiceImpl extends GenericServiceImpl<Currency> implements CurrencyService {

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pahappa.systems.core.services.GenericService#
	 * saveInstance(java.lang.Object)
     */
    @Override
    public Currency saveInstance(Currency instance) throws ValidationFailedException, OperationFailedException {
        if (StringUtils.isBlank(instance.getName())) {
            throw new ValidationFailedException("Missing a name.");
        }

        if (StringUtils.isBlank(instance.getSymbol())) {
            throw new ValidationFailedException("Missing a symbol.");
        }

        if (instance.getCountry() == null) {
            throw new ValidationFailedException("Missing a country");
        }
        // Validate the Currency by name
        {
            Currency existingByName = getCurrencyByName(instance.getName());
            if (existingByName != null && !existingByName.equals(instance)) {
                throw new ValidationFailedException("A currency with the specified name already exists.");
            }
        }

        // Validate the Currency by symbol
        {
            Currency existingBySymbol = getCurrencyBySymbol(instance.getSymbol());
            if (existingBySymbol != null && !existingBySymbol.equals(instance)) {
                throw new ValidationFailedException("A currency with the specified symbol already exists.");
            }

        }
        return super.save(instance);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pahappa.systems.akinamama.utils.backend.core.services.CurrencyService#
	 * getCurrencyByName(java.lang.String)
     */
    @Override
    public Currency getCurrencyByName(String name) {
        // TODO Auto-generated method stub
        return super.searchUniqueByPropertyEqual("name", name);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pahappa.systems.akinamama.utils.backend.core.services.CurrencyService#
	 * getCurrencyBySymbol(java.lang.String)
     */
    @Override
    public Currency getCurrencyBySymbol(String symbol) {
        return super.searchUniqueByPropertyEqual("symbol", symbol);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see org.pahappa.systems.akinamama.utils.backend.core.services.impl.
	 * GenericServiceImpl#isDeletable(org.sers.webutils.model.BaseEntity)
     */
    @Override
    public boolean isDeletable(Currency entity) throws OperationFailedException {
        return true;
    }

    @Override
    public Currency getCurrencyByCountry(Country country) {
        if(country==null){
        return null;
        }
        return super.searchUniqueByPropertyEqual("country", country);
    
    }
}
