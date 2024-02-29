package com.byaffe.learningking.daos;

import com.byaffe.learningking.models.BigDetails;
import com.byaffe.learningking.models.LookupValue;
import com.byaffe.learningking.shared.dao.BaseDAOImpl;
import org.springframework.stereotype.Repository;

/**
 * Data Access Object class for {@link BigDetails}
 */
@Repository
public class BigDetailsDaoImpl extends BaseDAOImpl<BigDetails> implements BigDetailsDao {
}

