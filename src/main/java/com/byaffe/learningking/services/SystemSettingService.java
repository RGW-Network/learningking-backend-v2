package com.byaffe.learningking.services;

import com.byaffe.learningking.models.SystemSetting;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.models.Country;

/**
 * Responsible for CRUD operations on {@link SystemSetting}
 * 
 * @author mmc
 *
 */
public interface SystemSettingService extends GenericService<SystemSetting>{
	/**
	 * Adds a {@link SystemSetting} to the database.
	 * 
	 * @param appSetting
     * @return 
	 * @throws ValidationFailedException if the following attributes are blank:
	 *               appName, serviceCode
	 */
	SystemSetting save(SystemSetting appSetting) throws ValidationFailedException;

	/**
	 * Gets mail settings
	 * 
	 * @return
	 */
	SystemSetting getAppSetting();

	/**
	 * Gets country by name
	 * 
	 * @return
	 */
	Country getCountryByName(String countryName);
}
