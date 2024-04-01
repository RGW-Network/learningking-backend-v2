package com.byaffe.learningking.daos;

import com.byaffe.learningking.models.LookupValue;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.dao.BaseDAOImpl;
import com.byaffe.learningking.shared.models.Country;
import org.springframework.stereotype.Repository;

/**
 * Data Access Object class for {@link LookupValue}
 */
@Repository
public class CountryDaoImpl extends BaseDAOImpl<Country> implements CountryDao {
    @Override
    public Country getByName(String string) {
        return super.searchUniqueByPropertyEqual("name",string, RecordStatus.ACTIVE);
    }
}

