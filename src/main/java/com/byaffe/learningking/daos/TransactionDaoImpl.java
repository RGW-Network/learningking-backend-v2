package com.byaffe.learningking.daos;

import com.byaffe.learningking.models.payments.AggregatorTransaction;
import com.byaffe.learningking.shared.dao.BaseDAOImpl;
import org.springframework.stereotype.Repository;

/**
 * Data Access Object class for {@link AggregatorTransaction}
 */
@Repository
public class TransactionDaoImpl  extends BaseDAOImpl<AggregatorTransaction> implements TransactionDao{
}

