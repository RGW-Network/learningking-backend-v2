package com.byaffe.learningking.services;

import com.byaffe.learningking.models.payments.Currency;
import com.byaffe.learningking.models.payments.CurrencyConversionRate;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.models.Country;

/**
 * @author Mzee Sr.
 *
 */
public interface CurrencyConversionRateService extends GenericService<CurrencyConversionRate> {

	/**
	 * 
	 * @param currencyConversionRate
	 * @throws OperationFailedException
	 */
	void activate(CurrencyConversionRate currencyConversionRate) throws OperationFailedException;

	/**
	 * 
	 * @param currencyConversionRate
	 * @throws OperationFailedException
	 */
	void deactivate(CurrencyConversionRate currencyConversionRate) throws OperationFailedException;

	/**
	 * 
	 * @param currency
	 * @return
	 */
	CurrencyConversionRate getActiveConversionRateToBaseCurrency(Currency currency);

        /**
         * 
         * @param from
         * @param to
         * @return
         * @throws OperationFailedException 
         */
	Float getConversionRateBetweenCurrecnies(Currency from, Currency to) throws OperationFailedException;

        /**
         * 
         * @param amount
         * @param from
         * @param to
         * @return
         * @throws OperationFailedException 
         */
	Float convertCurrency(Float amount, Currency from, Currency to) throws OperationFailedException;
        
        /**
         * 
         * @param country
         * @return 
         */
        CurrencyConversionRate getActiveConversionRateToBaseCurrency(Country country);
}
