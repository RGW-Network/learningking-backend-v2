package com.byaffe.learningking.services;

import com.byaffe.learningking.models.payments.Currency;
import com.byaffe.learningking.shared.models.Country;


/**
 * @author Mzee Sr.
 *
 */
public interface CurrencyService extends GenericService<Currency> {

	/**
	 * Gets a {@link Currency} by the specified name.
	 * 
	 * @param name
	 * @return
	 */
	Currency getCurrencyByName(String name);
        
        Currency getCurrencyByCountry(Country country);

	/**
	 * Gets a {@link Currency} by the specified symbol.
	 * 
	 * @param
	 * @return
	 */
	Currency getCurrencyBySymbol(String symbol);
}
