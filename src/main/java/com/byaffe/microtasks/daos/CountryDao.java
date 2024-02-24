package com.byaffe.microtasks.daos;

import com.byaffe.microtasks.models.LookupType;
import com.byaffe.microtasks.models.LookupValue;
import com.byaffe.microtasks.shared.dao.BaseDao;
import com.byaffe.microtasks.shared.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Data Access Object class for {@link LookupValue}
 */
public interface CountryDao extends BaseDao<Country> {
}

