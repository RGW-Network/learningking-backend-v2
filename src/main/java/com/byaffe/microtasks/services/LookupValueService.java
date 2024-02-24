package com.byaffe.microtasks.services;

import com.googlecode.genericdao.search.Search;
import com.byaffe.microtasks.dtos.LookupValueDTO;
import com.byaffe.microtasks.models.LookupType;
import com.byaffe.microtasks.models.LookupValue;
import com.byaffe.microtasks.shared.models.Country;

import java.util.List;

/**
 * Handles CRUD operations on the {@link  LookupValue}
 */
public interface LookupValueService {
    /**
     * Saves a microservice to the database
     * @return
     */
    LookupValue save(LookupValueDTO instance);

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

    List<Country> getCountries(Search search, int offset, int limit);

    long countCountries(Search search);
    long countLookupValues(Search search);

}
