package com.byaffe.microtasks.daos;

import com.byaffe.microtasks.models.BigDetails;
import com.byaffe.microtasks.models.LookupValue;
import com.byaffe.microtasks.shared.dao.BaseDAOImpl;
import com.byaffe.microtasks.shared.models.Country;
import org.springframework.stereotype.Repository;

/**
 * Data Access Object class for {@link BigDetails}
 */
@Repository
public class BigDetailsDaoImpl extends BaseDAOImpl<BigDetails> implements BigDetailsDao {
}

