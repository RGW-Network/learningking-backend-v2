package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.models.SystemSetting;
import com.byaffe.learningking.models.payments.Currency;
import com.byaffe.learningking.models.payments.CurrencyConversionRate;
import com.byaffe.learningking.models.payments.CurrencyConversionRateStatus;
import com.byaffe.learningking.services.CurrencyConversionRateService;
import com.byaffe.learningking.services.SystemSettingService;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.models.Country;
import com.googlecode.genericdao.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author Mzee Sr.
 *
 */
@Service
@Transactional
public class CurrencyConversionRateServiceImpl extends GenericServiceImpl<CurrencyConversionRate>
		implements CurrencyConversionRateService {

	@Autowired
	SystemSettingService systemSettingService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pahappa.systems.akinamama.utils.backend.core.services.GenericService#
	 * saveInstance(java.lang.Object)
	 */
	@Override
	public CurrencyConversionRate saveInstance(CurrencyConversionRate instance)
			throws ValidationFailedException, OperationFailedException {
		if (instance.getFromCurency() == null)
			throw new ValidationFailedException("Missing a from currency.");

		if (instance.getToBaseCurrency() <= 0)
			throw new ValidationFailedException("Conversion rate must be greater than ZERO(0).");

		SystemSetting activeSettings = systemSettingService.getAppSetting();

		if (activeSettings.getBaseCurrency() == null)
			throw new OperationFailedException("First set the base currency under System Settings and try again.");

		if (activeSettings.getBaseCurrency().equals(instance.getFromCurency()))
			throw new ValidationFailedException("The currency specified is he same as the base currency.");

		instance.setStatus(CurrencyConversionRateStatus.New);
		return super.save(instance);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.pahappa.systems.akinamama.utils.backend.core.services.
	 * CurrencyConversionRateService#activate(org.pahappa.systems.akinamama.utils.
	 * backend.models.currencies.CurrencyConversionRate)
	 */
	@Override
	public void activate(CurrencyConversionRate currencyConversionRate) throws OperationFailedException {
		SystemSetting activeSettings = systemSettingService.getAppSetting();

		if (activeSettings.getBaseCurrency() == null)
			throw new OperationFailedException("First set the base currency under System Settings and try again.");

		if (currencyConversionRate.getStatus().equals(CurrencyConversionRateStatus.New)) {

			if (getActiveConversionRateToBaseCurrency(currencyConversionRate.getFromCurency()) != null)
				throw new OperationFailedException(
						String.format("An active conversion rate already exists for the stated currency: %s",
								currencyConversionRate.getFromCurency().toString()));

			currencyConversionRate.setStatus(CurrencyConversionRateStatus.Active);
			currencyConversionRate.setDateActivated(new Date());
			super.save(currencyConversionRate);
		}

		throw new OperationFailedException(String.format("Already %s", currencyConversionRate.getStatus()));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.pahappa.systems.akinamama.utils.backend.core.services.
	 * CurrencyConversionRateService#deactivate(org.pahappa.systems.akinamama.utils.
	 * backend.models.currencies.CurrencyConversionRate)
	 */
	@Override
	public void deactivate(CurrencyConversionRate currencyConversionRate) throws OperationFailedException {
		if (currencyConversionRate.getStatus().equals(CurrencyConversionRateStatus.Deactive))
			throw new OperationFailedException("Already deactive");

		currencyConversionRate.setStatus(CurrencyConversionRateStatus.Deactive);
		currencyConversionRate.setDateDeactivated(new Date());
		super.save(currencyConversionRate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.pahappa.systems.akinamama.utils.backend.core.services.impl.
	 * GenericServiceImpl#isDeletable(com.byaffe.learningking.models)
	 */
	@Override
	public boolean isDeletable(CurrencyConversionRate entity) throws OperationFailedException {
		throw new OperationFailedException("Rates are simply deactivated.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.pahappa.systems.akinamama.utils.backend.core.services.
	 * CurrencyConversionRateService#getActiveConversionRate(org.pahappa.systems.
	 * akinamama.utils.backend.models.currencies.Currency)
	 */
	@Override
	public CurrencyConversionRate getActiveConversionRateToBaseCurrency(Currency currency) {
		return super.searchUnique(new Search().addFilterEqual("fromCurency", currency).addFilterEqual("status",
				CurrencyConversionRateStatus.Active));
	}

        
        /*
	 * (non-Javadoc)
	 * 
	 * @see org.pahappa.systems.akinamama.utils.backend.core.services.
	 * CurrencyConversionRateService#getActiveConversionRate(org.pahappa.systems.
	 * akinamama.utils.backend.models.currencies.Currency)
	 */
	@Override
	public CurrencyConversionRate getActiveConversionRateToBaseCurrency(Country country) {
		return super.searchUnique(new Search().addFilterEqual("fromCurency.country", country).addFilterEqual("status",
				CurrencyConversionRateStatus.Active));
	}

	@Override
	public Float getConversionRateBetweenCurrecnies(Currency from, Currency to) throws OperationFailedException {
		Currency baseCurrency = systemSettingService.getAppSetting().getBaseCurrency();
		CurrencyConversionRate fromCurrencyTobase = getActiveConversionRateToBaseCurrency(from);
		CurrencyConversionRate toCurrencyTobase = getActiveConversionRateToBaseCurrency(to);

		if (baseCurrency == null)
			throw new OperationFailedException("Base currency is not set in system settings.");

		if (fromCurrencyTobase == null)
			throw new OperationFailedException(
					String.format("No conversion rate exists from %s to the base currency - %s", from.getName(),
							baseCurrency.getName()));

		if (toCurrencyTobase == null)
			throw new OperationFailedException(
					String.format("No conversion rate exists from %s to the base currency - %s", to.getName(),
							baseCurrency.getName()));

		return fromCurrencyTobase.getToBaseCurrency() / toCurrencyTobase.getToBaseCurrency();
	}

	 @Override
	public Float convertCurrency(Float amount, Currency from, Currency to) throws OperationFailedException {
		Currency baseCurrency = systemSettingService.getAppSetting().getBaseCurrency();
		CurrencyConversionRate fromCurrencyTobase = getActiveConversionRateToBaseCurrency(from);
		CurrencyConversionRate toCurrencyTobase = getActiveConversionRateToBaseCurrency(to);

		if (baseCurrency == null)
			throw new OperationFailedException("Base currency is not set in system settings.");

		if (fromCurrencyTobase == null)
			throw new OperationFailedException(
					String.format("No conversion rate exists from %s to the base currency - %s", from.getName(),
							baseCurrency.getName()));

		if (toCurrencyTobase == null)
			throw new OperationFailedException(
					String.format("No conversion rate exists from %s to the base currency - %s", to.getName(),
							baseCurrency.getName()));

		return amount * (fromCurrencyTobase.getToBaseCurrency() / toCurrencyTobase.getToBaseCurrency());
	}


}
