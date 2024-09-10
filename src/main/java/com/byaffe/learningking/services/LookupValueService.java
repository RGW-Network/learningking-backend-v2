package com.byaffe.learningking.services;

import com.googlecode.genericdao.search.Search;
import com.byaffe.learningking.dtos.LookupValueDTO;
import com.byaffe.learningking.models.LookupType;
import com.byaffe.learningking.models.LookupValue;
import com.byaffe.learningking.shared.models.Country;

import java.util.List;

/**
 * Handles CRUD operations on the {@link  LookupValue}
 */
public interface LookupValueService {
    /**
     * Saves a microservice to the database
     * @return
     */
    LookupValue save(LookupValue instance);

     Country save(Country country );

    /**
     * Gets a list of microservices following a supplied search term, offset and limit
     * @param searchTerm
     * @param offset
     * @param limit
     * @return
     */
    List<LookupValue> getList(Search searchTerm, int offset, int limit);

    /**
     * Gets a microservice that matches a given Id
     * @param id
     * @return
     */
    LookupValue getById(long id);

    /**
     * Gets a microservice that matches a given code
     * @param member
     * @return
     */
    List<LookupValue> getByType(LookupType member);
    LookupValue getByType(LookupType lookupType,Long id);
    Country getCountryById(long id);

    List<Country> getCountries(Search search, int offset, int limit);

    long countCountries(Search search);
    long countLookupValues(Search search);

}
